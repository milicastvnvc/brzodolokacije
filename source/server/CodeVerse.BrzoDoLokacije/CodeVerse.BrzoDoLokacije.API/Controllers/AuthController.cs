using CodeVerse.BrzoDoLokacije.BL.Implementation.TokenHelper;
using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using CodeVerse.BrzoDoLokacije.Common.UserService;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Cryptography;

namespace CodeVerse.BrzoDoLokacije.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IUserBL _userBL;
        private readonly IUserService _userService;

        public AuthController(IUserBL userBL, IUserService userService)
        {
            _userBL = userBL;
            _userService = userService;
        }

        [HttpPost("Register")]
        public async Task<IActionResult> Register([FromBody] UserRegistrationViewModel userRegistration)
        {
            var result = await _userBL.AddUser(userRegistration);
            return Ok(result);
        }

        [HttpPost("Login")]
        public async Task<IActionResult> Login([FromBody] UserLoginViewModel userLogin)
        {
            var newRefreshToken = GenerateRefreshToken();
            var result = await _userBL.LoginUser(userLogin, newRefreshToken);

            var cookieOptions = new CookieOptions
            {
                HttpOnly = true,
                Expires = newRefreshToken.Expires
            };
            Response.Cookies.Append(ConfigProvider.RefreshTokenName, newRefreshToken.Token, cookieOptions);

            return Ok(result);
        }

        [HttpPost("VerifyUser")]
        public async Task<IActionResult> VerifyUser([FromBody] UserVerifyViewModel userVerify)
        {
            var result = await _userBL.VerifyUser(userVerify);
            return Ok(result);
        }

        [HttpPost("SendVerificationToken")]
        public async Task<IActionResult> SendVerificationToken([FromBody] VerificationTokenRequest sendVerificationTokenRequest)
        {
            var result = await _userBL.SendVerificationToken(sendVerificationTokenRequest.Email, sendVerificationTokenRequest.IsForgotPassword);
            return Ok(result);
        }

        [HttpPost("ResetPassword")]
        public async Task<IActionResult> ResetPassword([FromBody] ResetPasswordRequest resetPasswordRequest)
        {
            var result = await _userBL.ResetPassword(resetPasswordRequest);
            return Ok(result);
        }

        [HttpPost("RefreshToken"), Authorize]
        public async Task<IActionResult> RefreshToken(string refreshToken)
        {
            ActionResultResponse result = new ActionResultResponse();
            var userId = _userService.GetUserId();

            var user = await _userBL.GetUser(userId.Value);

            if (user == null)
            {
                return Unauthorized();
            }
            if (user.RefreshToken == null)
            {
                return Unauthorized();
            }
            else if (!user.RefreshToken.Equals(refreshToken))
            {
                return Unauthorized();
            }
            else if (user.RefreshTokenExpires < DateTime.Now)
            {
                return Unauthorized();
            }

            var newRefreshToken = GenerateRefreshToken();
            string jwt = await _userBL.CreateJWTToken(user, newRefreshToken);

            var cookieOptions = new CookieOptions
            {
                HttpOnly = true,
                Expires = newRefreshToken.Expires
            };
            Response.Cookies.Append(ConfigProvider.RefreshTokenName, newRefreshToken.Token, cookieOptions);

            result.ActionData = new { JwtToken = jwt, RefreshToken = newRefreshToken.Token };
            result.Success = true;

            return Ok(result);
        }

        private RefreshToken GenerateRefreshToken()
        {
            var refreshToken = new RefreshToken
            {
                Token = Common.Common.HashString(new PasswordGenerator.PasswordGenerator(6).IncludeUppercase().IncludeNumeric().Next()),
                Expires = DateTime.Now.AddDays(ConfigProvider.RefreshTokenExpiration),
                Created = DateTime.Now
            };

            return refreshToken;
        }
    }
}