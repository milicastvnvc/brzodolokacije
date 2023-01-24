using CodeVerse.BlogsDemo.API.BL.Implementation;
using CodeVerse.BlogsDemo.API.BL.Interfaces;
using CodeVerse.BlogsDemo.API.DAL.Context;
using CodeVerse.BlogsDemo.API.DAL.Implementation;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace CodeVerse.BlogsDemo.API.Common
{
    public static class ServiceInitializer
    {
        public static void Init(this IServiceCollection services)
        {
            services.AddDbContext<BlogsDbContext>(options => options.UseSqlite(ConfigProvider.ConnectionString));

            #region DAL
            services.AddScoped(typeof(IBaseDAL<>), typeof(BaseDAL<>));
            services.AddScoped<IBlogsDAL, BlogsDAL>();
            services.AddScoped<IPostsDAL, PostsDAL>();
            #endregion

            #region BL
            services.AddScoped<IPostsBL, PostsBL>();
            services.AddScoped<IBlogsBL, BlogsBL>();
            #endregion
        }
    }
}
