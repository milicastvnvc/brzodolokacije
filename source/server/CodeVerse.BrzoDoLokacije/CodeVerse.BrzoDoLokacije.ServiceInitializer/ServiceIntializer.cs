using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using Microsoft.Extensions.DependencyInjection;
using CodeVerse.BrzoDoLokacije.DAL.Implementation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.BL.Implementation;
using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using AutoMapper;
using CodeVerse.BrzoDoLokacije.Common.Mapper;
using CodeVerse.BrzoDoLokacije.Common.EmailService;
using Microsoft.IdentityModel.Tokens;
using Swashbuckle.AspNetCore.Filters;
using Microsoft.OpenApi.Models;
using CodeVerse.BrzoDoLokacije.Common.HttpClientHelper;
using CodeVerse.BrzoDoLokacije.Common.UserService;
using Microsoft.AspNetCore.Authentication.JwtBearer;

namespace CodeVerse.BrzoDoLokacije.ServiceInitializer
{
    public static class ServiceIntializer
    {
        public static void ServiceInit(this IServiceCollection services)
        {
            #region DbContext
            services.AddDbContext<BrzoDoLokacijeDbContext>(options =>
            {
                options.UseSqlite(ConfigProvider.ConnectionString, x => x.MigrationsAssembly("CodeVerse.BrzoDoLokacije.API"));
            });
            #endregion

            #region DAL
            services.AddScoped(typeof(IBaseDAL<>), typeof(BaseDAL<>));
            services.AddScoped<IUserDAL, UserDAL>();
            services.AddScoped<IPostDAL, PostDAL>();
            services.AddScoped<IAngularUserDAL, AngularUserDAL>();
            services.AddScoped<ICommentDAL, CommentDAL>();
            services.AddScoped<IRatingDAL, RatingDAL>();
            services.AddScoped<IResourceDAL, ResourceDAL>();
            services.AddScoped<ILikedUserDAL, LikedUserDAL>();
            #endregion

            #region BL
            services.AddScoped<IUserBL, UserBL>();
            services.AddScoped<IPostBL, PostBL>();
            services.AddScoped<IAngularUserBL, AngularUserBL>();
            #endregion

            #region Mapper
            services.AddAutoMapper(m => m.AddProfile(new MappingProfile()));
            #endregion

            #region CustomServices
            services.AddTransient<IEmailService, EmailService>();
            services.AddTransient<IHttpClientHelper, HttpClientHelper>();
            services.AddTransient<IUserService, UserService>();
            #endregion

            #region JWT
            services.AddHttpContextAccessor();

            services.AddSwaggerGen(options =>
            {
                options.AddSecurityDefinition("oauth2", new OpenApiSecurityScheme
                {
                    Description = "Standard Authorization header using the Bearer scheme (\"bearer {token}\")",
                    In = ParameterLocation.Header,
                    Name = "Authorization",
                    Type = SecuritySchemeType.ApiKey
                });

                options.OperationFilter<SecurityRequirementsOperationFilter>();
            });

            services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.SaveToken = true;
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(ConfigProvider.JwtTokenKey)),
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    ValidateLifetime = true,
                    ClockSkew = TimeSpan.Zero
                };
            });
            #endregion
        }
    }
}
