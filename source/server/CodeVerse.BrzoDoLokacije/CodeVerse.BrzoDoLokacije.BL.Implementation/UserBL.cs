using AutoMapper;
using CodeVerse.BrzoDoLokacije.BL.Implementation.TokenHelper;
using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using CodeVerse.BrzoDoLokacije.Common.Constants;
using CodeVerse.BrzoDoLokacije.Common.EmailService;
using CodeVerse.BrzoDoLokacije.Common.HttpClientHelper;
using CodeVerse.BrzoDoLokacije.Common.UserService;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using ImageMagick;
using Microsoft.AspNetCore.Http;
using Org.BouncyCastle.Asn1.Ocsp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Implementation
{
    public class UserBL : IUserBL
    {
        private readonly IUserDAL _userDAL;
        private readonly IMapper _mapper;
        private readonly IEmailService _emailService;
        private readonly IUserService _userService;
        private readonly ILikedUserDAL _likedUserDAL;
        private readonly IHttpClientHelper _http;
        private readonly IRatingDAL _ratingDAL;

        public UserBL(
            IUserDAL userDAL, 
            IMapper mapper, 
            IEmailService emailService, 
            IUserService userService, 
            ILikedUserDAL likedUserDAL,
            IHttpClientHelper http,
            IRatingDAL ratingDAL)
        {
            _userDAL = userDAL;
            _mapper = mapper;
            _emailService = emailService;
            _userService = userService;
            _likedUserDAL = likedUserDAL;
            _http = http;
            _ratingDAL = ratingDAL;
        }

        public async Task<ActionResultResponse> AddUser(UserRegistrationViewModel userRegistration)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            if (userRegistration == null)
            {
                actionResult.Success = false;
                return actionResult;
            }

            var userWithEmail = await _userDAL.GetByEmail(userRegistration.Email);

            if (userWithEmail != null)
            {
                actionResult.SetFalseSucces($"Korisnik sa email adresom {userRegistration.Email} postoji!");
                return actionResult;
            }

            var userWithUsername = await _userDAL.GetByUsername(userRegistration.Username);

            if (userWithUsername != null)
            {
                actionResult.SetFalseSucces($"Korisnik sa korisničkim imenom {userRegistration.Username} postoji!");
                return actionResult;
            }

            User user = _mapper.Map<User>(userRegistration);
            user.Password = BCrypt.Net.BCrypt.HashPassword(userRegistration.Password);

            var token = new PasswordGenerator.PasswordGenerator(6).IncludeUppercase().IncludeNumeric().Next();
            user.Token = token;
            user.TokenExpiration = DateTime.Now.AddMinutes(ConfigProvider.TokenVerificationExperationTime);
            user.IsVerified = false;

            var defaultProfilePicture = Common.Common.HashString($"DefaultPicture", $"DefaultPicture");
            var folderName = Path.Combine(ConfigProvider.StaticFilesFolder, defaultProfilePicture);
            var pathToSave = Path.Combine(Directory.GetCurrentDirectory(), folderName);
            var extension = Common.Common.GetFileExtension(ConfigProvider.DefaultUserPictureBase64);
            var fileName = $"{Constants.DefaultPictureName}.{extension}";
            var dbPath = Path.Combine(folderName, fileName);
            var fullPath = Path.Combine(pathToSave, fileName);

            if (!File.Exists(Path.Combine(pathToSave, Constants.DefaultPictureName))) 
            {
                Directory.CreateDirectory(pathToSave);

                byte[] bytes = Convert.FromBase64String(ConfigProvider.DefaultUserPictureBase64);

                using (var stream = new FileStream(fullPath, FileMode.Create))
                {
                    stream.Write(bytes, 0, bytes.Length);
                }
            }

            user.ProfilePicturePath = dbPath;

            await _userDAL.Insert(user);

            var body = ConfigProvider.RegistrationBody.Replace(ConfigProvider.TokenCodeReplace, token);
            _emailService.SendMail(new MailRequest { ToEmail = user.Email, Subject = "Uspešna registracija", Body = body });

            var returnUser = _mapper.Map<UserViewModel>(user);

            actionResult.Success = true;
            actionResult.ActionData = returnUser;
            return actionResult;
        }

        public async Task<string> CreateJWTToken(User user, RefreshToken newRefreshToken)
        {
            user.RefreshToken = newRefreshToken.Token;
            user.RefreshTokenExpires = newRefreshToken.Expires;
            user.RefreshTokenCreated = newRefreshToken.Created;

            await _userDAL.Update(user);

            return TokenHelper.TokenHelper.CreateToken(user);
        }

        public async Task<ActionResultResponse> DislikeUser(long userId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var currentUserId = _userService.GetUserId();

            if (currentUserId == null)
            {
                actionResult.SetFalseSucces("Niste ulogovani.");
                return actionResult;
            }

            var user = await _userDAL.Get(userId);

            if (user == null)
            {
                actionResult.SetFalseSucces("Korisnik kog zelite da dislajkujete ne postoji.");
                return actionResult;
            }

            var likedUser = await _likedUserDAL.GetLikedUser(currentUserId.Value, userId);

            if (likedUser == null)
            {
                actionResult.SetFalseSucces("Ne postoji trazeni lajk.");
                return actionResult;
            }

            await _likedUserDAL.Delete(likedUser);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task<List<UserViewModel>> GetCurrentUserLikedUsers(int pageIndex, int pageSize, string? searchTerm = null)
        {
            var userId = _userService.GetUserId();
            var users = await _likedUserDAL.GetLikedUsers(userId.Value, pageIndex, pageSize, searchTerm);
            var usersViewModels = _mapper.Map<List<UserViewModel>>(users);
            var usersRatings = _ratingDAL.GetAvgRatingForUsers(usersViewModels.Select(x => x.Id).ToList());

            for (int i = 0; i < usersViewModels.Count; i++)
            {
                var user = usersViewModels[i];
                user.IsLikedByCurrentUser = true;
                var userRatings = usersRatings.Where(x => x.UserId == user.Id).ToList();
                double sumRating = 0;
                int numberOfRatings = 0;

                foreach (var rating in userRatings)
                {
                    sumRating += rating.Ratings.Sum();
                    numberOfRatings = numberOfRatings + rating.Ratings.Count;
                }

                user.AvgRating = numberOfRatings > 0 ? sumRating / numberOfRatings : 0;
                user.NumberOfRatings = numberOfRatings;
            }

            return usersViewModels;
        }

        public async Task<User?> GetUser(long id)
        {
            return await _userDAL.Get(id);
        }

        public async Task<ActionResultResponse> GetUserById(long id)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var user = await _userDAL.Get(id);

            if (user == null)
            {
                actionResult.SetFalseSucces("Korisnik ne postoji!");
                return actionResult;
            }

            var currentUserId = _userService.GetUserId();

            if (currentUserId == null)
            {
                actionResult.SetFalseSucces("Korisnik nije ulogovan!");
                return actionResult;
            }

            actionResult.Success = true;
            
            var userViewModel = _mapper.Map<UserViewModel>(user);
            var isLikedByCurrentUser = await _likedUserDAL.GetLikedUser(currentUserId.Value, id);
            userViewModel.IsLikedByCurrentUser = isLikedByCurrentUser != null ? true : false;
            actionResult.ActionData = userViewModel;

            return actionResult;
        }

        public async Task<ActionResultResponse> LikeUser(long userId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var currentUserId = _userService.GetUserId();

            if (currentUserId == null)
            {
                actionResult.SetFalseSucces("Niste ulogovani.");
                return actionResult;
            }

            var user = await _userDAL.Get(userId);

            if (user == null)
            {
                actionResult.SetFalseSucces("Korisnik kog zelite da lajkujete ne postoji.");
                return actionResult;
            }

            LikedUser likedUser = new LikedUser();
            likedUser.UserId = userId;
            likedUser.LikedById = currentUserId.Value;
            likedUser.CreatedDate = DateTime.Now;

            await _likedUserDAL.Insert(likedUser);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task<ActionResultResponse> LoginUser(UserLoginViewModel userLogin, RefreshToken newRefreshToken)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var userExist = await _userDAL.GetByUsername(userLogin.Username);

            if (userExist == null)
            {
                actionResult.SetFalseSucces($"Ne postoji nalog sa {userLogin.Username} korisničkim imenom!");
                return actionResult;
            }

            var passwordVerified = BCrypt.Net.BCrypt.Verify(userLogin.Password, userExist.Password);

            if (!passwordVerified)
            {
                actionResult.SetFalseSucces($"Pogrešna lozinka!");
                return actionResult;
            }


            if (!userExist.IsVerified.HasValue || (userExist.IsVerified.HasValue && !userExist.IsVerified.Value))
            {
                if (userExist.TokenExpiration.HasValue && !(userExist.TokenExpiration.Value > DateTime.Now))
                {
                    var verificationToken = new PasswordGenerator.PasswordGenerator(6).IncludeUppercase().IncludeNumeric().Next();

                    userExist.Token = verificationToken;
                    userExist.TokenExpiration = DateTime.Now.AddMinutes(ConfigProvider.TokenVerificationExperationTime);
                    userExist.IsVerified = false;
                    var user = _mapper.Map<UserViewModel>(userExist);

                    await _userDAL.Update(userExist);

                    var body = ConfigProvider.VerificationBody.Replace(ConfigProvider.TokenCodeReplace, verificationToken);
                    _emailService.SendMail(new MailRequest { ToEmail = userExist.Email, Subject = "Verifikacija", Body = body });

                    actionResult.ActionData = new { User = user, Token = "" };
                    actionResult.SetFalseSucces($"Morate se verifikovati! Poslat vam je nov email sa kodom.");
                }
                else
                {
                    var user = _mapper.Map<UserViewModel>(userExist);
                    actionResult.ActionData = new { User = user, Token = "" };
                    actionResult.SetFalseSucces($"Morate se verifikovati! Proverite email za kod!");
                }
            }
            else
            {
                var user = _mapper.Map<UserViewModel>(userExist);
                var token = TokenHelper.TokenHelper.CreateToken(userExist);

                userExist.RefreshToken = newRefreshToken.Token;
                userExist.RefreshTokenExpires = newRefreshToken.Expires;
                userExist.RefreshTokenCreated = newRefreshToken.Created;

                await _userDAL.Update(userExist);

                actionResult.Success = true;
                actionResult.ActionData = new { User = user, Token = token, RefreshToken = newRefreshToken.Token };
            }

            return actionResult;
        }

        public async Task<ActionResultResponse> ResetPassword(ResetPasswordRequest resetPasswordRequest)
        {
            ActionResultResponse actionResult = new ActionResultResponse();
            var user = await _userDAL.GetByEmail(resetPasswordRequest.Email);

            if (user == null)
            {
                actionResult.SetFalseSucces($"Korisnik sa email adresom {resetPasswordRequest.Email} ne postoji");
                return actionResult;
            }

            if ((user.Token.ToLower().Trim() != resetPasswordRequest.Token.ToLower().Trim()) ||
                (!user.TokenExpiration.HasValue || (user.TokenExpiration.HasValue && user.TokenExpiration < DateTime.Now)))
            {
                actionResult.SetFalseSucces($"Token nije validan!");
                return actionResult;
            }

            user.Password = BCrypt.Net.BCrypt.HashPassword(resetPasswordRequest.Password);
            await _userDAL.Update(user);
            _emailService.SendMail(new MailRequest
            { ToEmail = user.Email, Subject = "Promena lozinke", Body = ConfigProvider.ForgotPasswordSuccesBody });
            actionResult.Success = true;

            return actionResult;
        }

        public async Task<ActionResultResponse> SendVerificationToken(string email, bool? isForgotPassword = false)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var user = await _userDAL.GetByEmail(email);

            if (user == null)
            {
                actionResult.SetFalseSucces($"Korisnik sa email adresom {email} ne postoji!");
                return actionResult;
            }

            var verificationToken = new PasswordGenerator.PasswordGenerator(6).IncludeUppercase().IncludeNumeric().Next();
            user.Token = verificationToken;
            user.TokenExpiration = DateTime.Now.AddMinutes(ConfigProvider.TokenVerificationExperationTime);

            if (isForgotPassword.HasValue && isForgotPassword.Value)
            {
                var body = ConfigProvider.ForgotPasswordVerificationBody.Replace(ConfigProvider.TokenCodeReplace, verificationToken);
                _emailService.SendMail(new MailRequest { ToEmail = user.Email, Subject = "Zaboravili ste lozinku", Body = body });
            }
            else
            {
                var body = ConfigProvider.VerificationBody.Replace(ConfigProvider.TokenCodeReplace, verificationToken);
                _emailService.SendMail(new MailRequest { ToEmail = user.Email, Subject = "Verifikacija", Body = body });
            }

            await _userDAL.Update(user);

            actionResult.Success = true;

            return actionResult;
        }

        public async Task<ActionResultResponse> UpdateUser(UpdateUserRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();
            var changes = false;

            var userExists = await _userDAL.Get(request.Id);

            if (userExists == null)
            {
                actionResult.SetFalseSucces($"Korisnik ne postoji!");
                return actionResult;
            }

            if (!userExists.FirstName.Equals(request.FirstName)) 
            { 
                userExists.FirstName = request.FirstName;
                changes = true;
            }

            if (!userExists.LastName.Equals(request.LastName))
            {
                userExists.LastName = request.LastName;
                changes = true;
            }

            if (!userExists.Username.Equals(request.Username))
            {
                var userWithSameUsername = await _userDAL.GetByUsername(request.Username);

                if (userWithSameUsername != null)
                {
                    actionResult.SetFalseSucces("Korisnicko ime je zauzeto");
                    return actionResult;
                }
                else
                {
                    userExists.Username = request.Username;
                    changes = true;
                }
            }

            if (!string.IsNullOrEmpty(request.ProfilePictureBase64) && request.ChangeProfilePicture)
            {
                var fileExtension = Common.Common.GetFileExtension(request.ProfilePictureBase64);

                if (!ConfigProvider.AllowedPictureExtensions.Contains(fileExtension.ToUpper()))
                {
                    actionResult.Success = false;
                    actionResult.SetFalseSucces("Format slike nije podrzan!");
                    return actionResult;
                }

                var hasOneFace = await _http.PostAsync<bool>(
                        $"{ConfigProvider.WebServiceUrl}/has-one-face-on-image/", new { base64 = request.ProfilePictureBase64 });

                if (!hasOneFace)
                {
                    actionResult.Success = false;
                    actionResult.SetFalseSucces("Na slici ne postoji lice ili ima vise od jednog lica.");
                    return actionResult;
                }

                string userResourceFolder = Common.Common.HashString($"Images{userExists.Id}ProfilePictures", $"Images{userExists.Id}ProfilePictures");
                var folderName = Path.Combine(ConfigProvider.StaticFilesFolder, userResourceFolder);
                var pathToSave = Path.Combine(Directory.GetCurrentDirectory(), folderName);

                Directory.CreateDirectory(pathToSave);

                var fileName = new PasswordGenerator.PasswordGenerator(30).IncludeNumeric().IncludeUppercase().Next();
                fileName = $"{fileName}.{fileExtension}";

                string newFileName;
                string dbPath;

                byte[] bytes = Convert.FromBase64String(request.ProfilePictureBase64);

                using (MagickImage image = new MagickImage(bytes))
                {
                    image.Quality = ConfigProvider.ImageQuality;
                    if (image.Width > ConfigProvider.ImageWidth)
                    {
                        double widthRatio = (double)ConfigProvider.ImageWidth / (double)image.Width;
                        int newHeight = Convert.ToInt32(widthRatio * image.Height);
                        image.Resize(ConfigProvider.ImageWidth, newHeight);
                        image.Format = MagickFormat.Jpeg;
                    }

                    newFileName = $"{fileName.Split(".")[0]}.{ConfigProvider.DefaultPictureExtension}";
                    var fullPath = Path.Combine(pathToSave, newFileName);
                    dbPath = Path.Combine(folderName, newFileName);

                    image.Write(fullPath);
                }

                userExists.ProfilePicturePath = dbPath;
                changes = true;
            }

            
            if (changes)
            {
                await _userDAL.Update(userExists);
            }

            actionResult.Success = true;
            actionResult.ActionData = _mapper.Map<UserViewModel>(userExists);
            return actionResult;
        }

        public async Task<ActionResultResponse> VerifyUser(UserVerifyViewModel userVerify)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var user = await _userDAL.GetByEmail(userVerify.Email);

            if (user == null)
            {
                actionResult.SetFalseSucces($"Korisnik sa email adresom {userVerify.Email} ne postoji!");
                return actionResult;
            }

            if (string.IsNullOrEmpty(userVerify.Token))
            {
                actionResult.SetFalseSucces($"Greška!");
                return actionResult;
            }

            if (userVerify.Token.ToLower().Trim() != user.Token.ToLower().Trim())
            {
                actionResult.SetFalseSucces($"Token nije ispravan!");
                return actionResult;
            }

            if (user.TokenExpiration.HasValue && user.TokenExpiration < DateTime.Now)
            {
                actionResult.SetFalseSucces($"Token je istekao!");
                return actionResult;
            }

            user.IsVerified = true;
            await _userDAL.Update(user);
            actionResult.Success = true;

            _emailService.SendMail(new MailRequest { ToEmail = user.Email, Subject = "Uspešna verifikacija", Body = ConfigProvider.VerificationSuccesBody });

            return actionResult;
        }
    }
}