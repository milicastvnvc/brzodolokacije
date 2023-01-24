using AutoMapper;
using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using CodeVerse.BrzoDoLokacije.Common.Constants;
using CodeVerse.BrzoDoLokacije.Common.HttpClientHelper;
using CodeVerse.BrzoDoLokacije.Common.UserService;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ImageRecognition;
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
    public class PostBL : IPostBL
    {
        private readonly IPostDAL _postDAL;
        private readonly IUserService _userService;
        private readonly IHttpClientHelper _http;
        private readonly IMapper _mapper;
        private readonly ICommentDAL _commentDAL;
        private readonly IRatingDAL _ratingDAL;
        private readonly IResourceDAL _resourceDAL;
        private readonly ILikedUserDAL _likedUserDAL;

        public PostBL(IPostDAL postDAL, 
            IUserService userService, 
            IHttpClientHelper http, 
            IMapper mapper, 
            ICommentDAL commentDAL, 
            IRatingDAL ratingDAL,
            IResourceDAL resourceDAL,
            ILikedUserDAL likedUserDAL)
        {
            _postDAL = postDAL;
            _userService = userService;
            _http = http;
            _mapper = mapper;
            _commentDAL = commentDAL;
            _ratingDAL = ratingDAL;
            _resourceDAL = resourceDAL;
            _likedUserDAL = likedUserDAL;
        }

        public async Task<UserProfileResponse> GetPostsByUserId(long userId, int pageIndex, int pageSize)
        {
            UserProfileResponse response = new UserProfileResponse();
            var posts = _postDAL.GetPostsByUserId(userId, pageIndex, pageSize, out int total);
            var avgRating = _ratingDAL.GetAvgRatingForUser(userId, out int numberOfRatings);
            response.Posts = _mapper.Map<List<PostViewModel>>(posts);
            response.AvgRating = avgRating;
            response.NumberOfRatings = numberOfRatings;
            response.NumberOfPosts = total;
            return response;
        }

        public async Task<List<Coordinate>> GetAllCoordinates()
        {
            return await _postDAL.GetAllCoordinates();
        }

        public async Task<List<PostViewModel>> GetPosts(GetPostsRequest request)
        {
            var posts = _postDAL.GetPosts(request, out int total);
            var postViewModels = _mapper.Map<List<PostViewModel>>(posts);
            return postViewModels;
        }

        public async Task<ActionResultResponse> DeletePost(long postId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();
            var post = await _postDAL.Get(postId);
            var currentUser = _userService.GetUserId();

            if (post == null)
            {
                actionResult.SetFalseSucces($"Objava koja ima id {postId} ne postoji!");
                return actionResult;
            }

            if (currentUser == null)
            {
                actionResult.SetFalseSucces("Korisnik nije ulogovan");
                return actionResult;
            }

            if (currentUser.Value != post.CreatedById)
            {
                actionResult.SetFalseSucces("Ne mozete obrisati tudju objavu");
                return actionResult;
            }

            var resources = await _resourceDAL.GetResourcesByPostId(postId);

            foreach (var resource in resources)
            {
                string fullPath = Path.Combine(Directory.GetCurrentDirectory(), resource.Path);

                if (File.Exists(fullPath))
                {
                    File.Delete(fullPath);
                }
            }

            await _postDAL.Delete(post);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task<ActionResultResponse> GetPost(long postId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var post = await _postDAL.GetPost(postId);
            var userId = _userService.GetUserId();

            if (post == null)
            {
                actionResult.SetFalseSucces($"Objava koja ima id {postId} ne postoji.");
                return actionResult;
            }

            if (userId == null)
            {
                actionResult.SetFalseSucces("Niste ulogovani!");
                return actionResult;
            }

            var postViewModel = _mapper.Map<PostViewModel>(post);

            postViewModel.Comments = _mapper.Map<List<CommentViewModel>>(await _commentDAL.GetCommentsForPost(postId));

            var avgRating = _ratingDAL.GetAvgRatingForPost(postId, out int numberOfRatings);
            var userRating = await _ratingDAL.GetRating(postId, userId.Value);

            var isUserLikedByCurrent = await _likedUserDAL.GetLikedUser(userId.Value, post.CreatedById);

            postViewModel.NumberOfRatings = numberOfRatings;
            postViewModel.AvgRating = avgRating;
            postViewModel.UserRate = userRating != null ? userRating.Rate : null;
            postViewModel.CreatedBy.IsLikedByCurrentUser = isUserLikedByCurrent != null ? true : false;

            actionResult.ActionData = postViewModel;
            actionResult.Success = true;
            return actionResult;
        }

        public async Task<ActionResultResponse> AddComment(AddCommentRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var post = await _postDAL.GetPost(request.PostId);

            if (post == null)
            {
                actionResult.SetFalseSucces($"Objava koja ima id {request.PostId} ne postoji.");
                return actionResult;
            }

            var userId = _userService.GetUserId();

            if (request.ParentCommentId != null)
            {
                var parentComment = await _commentDAL.Get(request.ParentCommentId.Value);
                if (parentComment == null)
                {
                    actionResult.SetFalseSucces($"Ne postoji roditeljski komentar.");
                    return actionResult;
                }
            }

            Comment comment = new Comment();
            comment.Text = request.Text;
            comment.ParentCommentId = request.ParentCommentId;
            comment.CreatedDate = DateTime.Now;
            comment.CreatedById = userId.Value;
            comment.PostId = request.PostId;

            await _commentDAL.Insert(comment);
            actionResult.Success = true;

            return actionResult;
        }

        public async Task<ActionResultResponse> DeleteComment(long commentId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var comment = await _commentDAL.GetComment(commentId);

            if (comment == null)
            {
                actionResult.SetFalseSucces($"Ne mozete obrisati komentar koji ne postoji!");
                return actionResult;
            }

            var userId = _userService.GetUserId();

            if (userId != comment.CreatedById && userId != comment.Post.CreatedById) 
            {
                actionResult.SetFalseSucces("Ne mozete da obrisete tudj komentar");
                return actionResult;
            }

            await _commentDAL.Delete(comment);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task<ActionResultResponse> UpdateComment(UpdateCommentRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var comment = await _commentDAL.Get(request.CommentId);

            if (comment == null )
            {
                actionResult.SetFalseSucces("Komentar ne postoji");
                return actionResult;
            }

            comment.Text = request.Text;
            await _commentDAL.Update(comment);

            actionResult.Success = true;
            return actionResult;
        }

        public async Task<ActionResultResponse> RatePost(RatePostRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var post = await _postDAL.Get(request.PostId);

            if (post == null)
            {
                actionResult.SetFalseSucces($"Objava koja ima id {request.PostId} ne postoji!");
                return actionResult;
            }

            var userId = _userService.GetUserId();

            if (!userId.HasValue)
            {
                actionResult.SetFalseSucces($"Niste ulogovani!");
                return actionResult;
            }

            var ratingExists = await _ratingDAL.GetRating(post.Id, userId.Value);

            if (ratingExists != null)
            {
                ratingExists.Rate = request.Rate;

                await _ratingDAL.Update(ratingExists);

                actionResult.Success = true;
                actionResult.ActionData = _ratingDAL.GetAvgRatingForPost(request.PostId, out int numberOfRatings1);
                return actionResult;
            }

            if (request.Rate < 1 || request.Rate > 5)
            {
                actionResult.SetFalseSucces($"Ocene mogu biti samo od 1 do 5.");
                return actionResult;
            }

            Rating rating = new Rating()
            {
                PostId = request.PostId,
                UserId = userId.Value,
                Rate = request.Rate,
                CreatedDate = DateTime.Now
            };

            await _ratingDAL.Insert(rating);

            actionResult.Success = true;
            actionResult.ActionData = _ratingDAL.GetAvgRatingForPost(request.PostId, out int numberOfRatings2);
            return actionResult;
        }

        public async Task<ActionResultResponse> RemoveRateFromPost(long postId)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var post = await _postDAL.Get(postId);

            if (post == null)
            {
                actionResult.SetFalseSucces($"Objava koja ima id {postId} ne postoji.");
                return actionResult;
            }

            var userId = _userService.GetUserId();

            if (userId == null)
            {
                actionResult.SetFalseSucces("Niste ulogovani!");
                return actionResult;
            }

            var rating = await _ratingDAL.GetRating(postId, userId.Value);

            if (rating == null)
            {
                actionResult.SetFalseSucces("Ne postoji ocena koju zelite da ponistite.");
                return actionResult;
            }

            await _ratingDAL.Delete(rating);

            actionResult.Success = true;
            actionResult.ActionData = _ratingDAL.GetAvgRatingForPost(postId, out int numberOfRatings);
            return actionResult;
        }

        public async Task<List<Coordinate>> GetUserPostsCoordinates(long userId)
        {
            return await _postDAL.GetUserPostsCoordinates(userId);
        }

        public async Task<List<PostViewModel>> GetPostsByCoordinates(double latitude, double longitude)
        {
            var posts = await _postDAL.GetPostsByCoordinates(latitude, longitude);
            return _mapper.Map<List<PostViewModel>>(posts);
        }

        public async Task<ActionResultResponse> AddImagesForPost(long postId, Dictionary<string, byte[]> images)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var post = await _postDAL.Get(postId);

            if (post == null)
            {
                actionResult.SetFalseSucces($"Ne postoji post koji ima id {postId}");
                return actionResult;
            }

            var userId = _userService.GetUserId();

            if (userId == null)
            {
                actionResult.SetFalseSucces($"Ne postoji korisnik");
                return actionResult;
            }

            var response = await _http.PostFileAsync<List<ImageRecognitionResponse>>
                ($"{ConfigProvider.WebServiceUrl}/has-face-on-image/", images);

            if (response == null)
            {
                await _postDAL.Delete(post);
                actionResult.SetFalseSucces("Greska prilikom provere slika.");
                return actionResult;
            }

            foreach (var res in response)
            {
                if (res.HasFace)
                {
                    await _postDAL.Delete(post);
                    actionResult.SetFalseSucces("Na slici ne sme da bude osoba.");
                    return actionResult;
                }
            }

            List<Resource> resources = new List<Resource>();

            string userResourceFolder = Common.Common.HashString($"Images{userId.Value}", $"Images{userId.Value}");
            string folderName = Path.Combine(ConfigProvider.StaticFilesFolder, userResourceFolder);
            string pathToSave = Path.Combine(Directory.GetCurrentDirectory(), folderName);

            Directory.CreateDirectory(pathToSave);

            foreach (var file in images)
            {
                string fileName = new PasswordGenerator.PasswordGenerator(30).IncludeNumeric().IncludeUppercase().Next();
                fileName = $"{fileName}.{file.Key.Split(".")[1].ToLower().Trim()}";
                string dbPath = Path.Combine(folderName, fileName);
                string fullPath = Path.Combine(pathToSave, fileName);

                using (var stream = new FileStream(fullPath, FileMode.Create))
                {
                    stream.Write(file.Value, 0, file.Value.Length);
                    stream.Flush();
                }

                resources.Add(new Resource
                {
                    Name = fileName,
                    Path = dbPath,
                    IsPhoto = true,
                    PostId = postId
                });
            }

            await _resourceDAL.AddRange(resources);

            actionResult.Success = true;

            return actionResult;
        }

        public async Task<ActionResultResponse> CreateNewPost(AddPostRequest request)
        {
            ActionResultResponse actionResult = new ActionResultResponse();

            var userId = _userService.GetUserId();

            if (userId == null)
            {
                actionResult.SetFalseSucces($"Korisnik ne postoji!");
                return actionResult;
            }

            Post post = new Post();

            StringBuilder stringBuilder = new StringBuilder();
            foreach (var tag in request.Tags)
            {
                stringBuilder.Append($"{tag.ToLower().Trim()}{Constants.TagSpliter}");
            }

            post.Description = request.Description;
            post.DateCreated = DateTime.Now;
            post.CreatedById = userId.Value;
            post.LongitudeCenter = request.LongitudeCenter.ToString();
            post.LatitudeCenter = request.LatitudeCenter.ToString();
            post.PlaceName = request.PlaceName;
            post.PlaceNameSr = request.PlaceNameSr;
            post.Text = request.Text;
            post.Tags = stringBuilder.ToString();
            post.PostSearchTerm = string.Empty;
            post.PostSearchTerm = request.PlaceName != null ? post.PostSearchTerm + " " + request.PlaceName : string.Empty;
            post.PostSearchTerm = request.PlaceNameSr != null ? post.PostSearchTerm + " " + request.PlaceNameSr : string.Empty;
            post.PostSearchTerm = request.Text != null ? post.PostSearchTerm + " " + request.Text : string.Empty;

            if (string.IsNullOrEmpty(post.PostSearchTerm))
            {
                post.PostSearchTerm = null;
            }
            else
            {
                post.PostSearchTerm = post.PostSearchTerm.ToUpper().Trim();
            }

            await _postDAL.Insert(post);

            actionResult.ActionData = post.Id;
            actionResult.Success = true;
            return actionResult;
        }
    }
}