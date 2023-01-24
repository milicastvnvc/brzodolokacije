using CodeVerse.BrzoDoLokacije.Common.EmailService;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.ConfigProvider
{
    public static class ConfigProvider
    {
        public static string ConnectionString { get; private set; }
        public static MailSettings MailSettings { get; private set; }
        public static int TokenVerificationExperationTime { get; private set; }
        public static string RegistrationBody { get; private set; }
        public static string VerificationBody { get; private set; }
        public static string ForgotPasswordVerificationBody { get; private set; }
        public static string ForgotPasswordSuccesBody { get; private set; }
        public static string VerificationSuccesBody { get; private set; }
        public static string WebServiceUrl { get; private set; }
        public static string JwtTokenKey { get; private set; }
        public static int JwtTokenExpirationTime { get; private set; }
        public static string TokenCodeReplace { get; private set; }
        public static string HeadingReplace { get; private set; }
        public static string StaticFilesFolder { get; private set; }
        public static List<string> AllowedPictureExtensions { get; private set; }
        public static List<string> AllowedVideoExtensions { get; private set; }
        public static string BackendUrl { get; private set; }
        public static int RefreshTokenExpiration { get; private set; }
        public static string RefreshTokenName { get; private set; }
        public static int NumberOfResourcesForPost { get; private set; }
        public static string DefaultUserPictureBase64 { get; private set; }
        public static int ImageWidth { get; private set; }
        public static int ImageQuality { get; private set; }
        public static string DefaultPictureExtension { get; private set; }

        public static void ConfigInit(IConfiguration configuration)
        {
            ConnectionString = configuration["ConnectionStrings:SqliteConnection"];
            MailSettings = new MailSettings
            {
                Mail = configuration["MailSettings:Mail"],
                Host = configuration["MailSettings:Host"],
                Password = configuration["MailSettings:Password"],
                Port = Convert.ToInt32(configuration["MailSettings:Port"])
            };
            TokenVerificationExperationTime = Convert.ToInt32(configuration["TokenVerificationExperationTime"]);
            RegistrationBody = configuration["MailBody:RegistrationBody"];
            VerificationBody = configuration["MailBody:VerificationBody"];
            ForgotPasswordVerificationBody = configuration["MailBody:ForgotPasswordVerificationBody"];
            ForgotPasswordSuccesBody = configuration["MailBody:ForgotPasswordSuccesBody"];
            VerificationSuccesBody = configuration["MailBody:VerificationSuccesBody"];
            WebServiceUrl = configuration["WebServiceUrl"];
            JwtTokenKey = configuration["JwtToken:Key"];
            JwtTokenExpirationTime = Convert.ToInt32(configuration["JwtToken:ExpirationTime"]);
            TokenCodeReplace = configuration["MailBody:TokenCodeReplace"];
            HeadingReplace = configuration["MailBody:HeadingReplace"];
            StaticFilesFolder = configuration["StaticFilesFolder"];
            AllowedPictureExtensions = configuration.GetSection("AllowedPictureExtensions").Get<List<string>>();
            BackendUrl = configuration["BackendUrl"];
            RefreshTokenExpiration = Convert.ToInt32(configuration["RefreshToken:ExpirationTime"]);
            RefreshTokenName = configuration["RefreshToken:Name"];
            NumberOfResourcesForPost = Convert.ToInt32(configuration["NumberOfResourcesForPost"]);
            DefaultUserPictureBase64 = configuration["DefaultUserPictureBase64"];
            AllowedVideoExtensions = configuration.GetSection("AllowedVideoExtensions").Get<List<string>>();
            ImageWidth = int.Parse(configuration["ImageWidth"]);
            ImageQuality = int.Parse(configuration["ImageQuality"]);
            DefaultPictureExtension = configuration["DefaultPictureExtension"];
        }
    }
}