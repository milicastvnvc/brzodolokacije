using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.UserService
{
    public class UserService : IUserService
    {
        private readonly IHttpContextAccessor _httpContextAccessor;

        public UserService(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        public long? GetUserId()
        {
            long? userId = null;
            if (_httpContextAccessor.HttpContext != null)
            {
                userId = _httpContextAccessor.HttpContext.User.FindFirstValue("Id") != null ? 
                    long.Parse(_httpContextAccessor.HttpContext.User.FindFirstValue("Id")) : null;
            }
            return userId;
        }

        public string? GetUserToken()
        {
            string? userToken = null;
            if (_httpContextAccessor.HttpContext != null)
            {
                userToken = _httpContextAccessor.HttpContext.Request.Headers.Authorization.Count > 0 ?
                _httpContextAccessor.HttpContext.Request.Headers.Authorization[0].Split(" ").Last() : null;
            }
            return userToken;
        }
    }
}
