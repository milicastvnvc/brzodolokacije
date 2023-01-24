using CodeVerse.BrzoDoLokacije.BL.Implementation.TokenHelper;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Interfaces
{
    public interface IUserBL
    {
        public Task<ActionResultResponse> AddUser(UserRegistrationViewModel userRegistration);
        public Task<ActionResultResponse> LoginUser(UserLoginViewModel userLogin, RefreshToken newRefreshToken);
        public Task<ActionResultResponse> VerifyUser(UserVerifyViewModel userVerify);
        public Task<ActionResultResponse> SendVerificationToken(string email, bool? isForgotPassword = false);
        public Task<ActionResultResponse> ResetPassword(ResetPasswordRequest resetPasswordRequest);
        public Task<ActionResultResponse> UpdateUser(UpdateUserRequest user);
        public Task<User?> GetUser(long id);
        public Task<string> CreateJWTToken(User user, RefreshToken newRefreshToken);
        public Task<ActionResultResponse> LikeUser(long userId);
        public Task<ActionResultResponse> DislikeUser(long userId);
        public Task<List<UserViewModel>> GetCurrentUserLikedUsers(int pageIndex, int pageSize, string? searchTerm = null);
        public Task<ActionResultResponse> GetUserById(long id);
    }
}