using CodeVerse.BlogsDemo.API.DAL.Context;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;
using Microsoft.EntityFrameworkCore;

namespace CodeVerse.BlogsDemo.API.DAL.Implementation
{
    public class PostsDAL : BaseDAL<Post>, IPostsDAL
    {
        public PostsDAL(BlogsDbContext context) : base(context)
        {
        }

        public async Task<Post?> GetPostByTitle(string title)
        {
            return await Table.Where(p => p.Title == title).FirstOrDefaultAsync();
        }

        public async Task<List<Post>> GetPostsByBlogId(long blogId)
        {
            return await Table.Where(p => p.BlogId == blogId).ToListAsync();
        }
    }
}
