namespace CodeVerse.BlogsDemo.API.Common
{
    public class ConfigProvider
    {
        public static string ConnectionString { get; private set; }

        public static void SetupConfiguration(IConfiguration configuration)
        {
            ConnectionString = configuration["ConnectionStrings:SqliteConnStr"];
        }
    }
}
