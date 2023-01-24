using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using CodeVerse.BrzoDoLokacije.Common.Constants;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Implementation.TokenHelper
{
    public static class TokenHelper
    {
        public static string CreateToken(User? user = null, AngularUser? angularUser = null)
        {
            List<Claim> claims;

            if (angularUser != null)
            {
                claims = new List<Claim>
                {
                    new Claim("Id", angularUser.Id.ToString()),
                    new Claim(ClaimTypes.Name, angularUser.Username),
                    new Claim(ClaimTypes.Role, Constants.AngularUserRole)
                };
            }
            else
            {
                claims = new List<Claim>
                {
                    new Claim("Id", user.Id.ToString()),
                    new Claim(ClaimTypes.Name, user.Username),
                    new Claim(ClaimTypes.Email, user.Email),
                    new Claim(ClaimTypes.Role, Constants.UserRole)
                };
            }
            

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(ConfigProvider.JwtTokenKey));

            var creds = new SigningCredentials(key, "HS512");

            var token = new JwtSecurityToken(
                claims: claims,
                expires: DateTime.Now.AddDays(ConfigProvider.JwtTokenExpirationTime),
                signingCredentials: creds);

            var jwt = new JwtSecurityTokenHandler().WriteToken(token);

            return jwt;
        }
    }
}
