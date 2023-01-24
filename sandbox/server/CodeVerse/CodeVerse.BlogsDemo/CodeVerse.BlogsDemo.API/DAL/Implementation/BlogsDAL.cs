using CodeVerse.BlogsDemo.API.DAL.Context;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;
using Microsoft.EntityFrameworkCore;

namespace CodeVerse.BlogsDemo.API.DAL.Implementation
{
    public class BlogsDAL : BaseDAL<Blog>, IBlogsDAL
    {
        public BlogsDAL(BlogsDbContext context) : base(context)
        {
        }

        public async Task<Blog?> GetBlogByTitle(string title)
        {
            return await Table.Where(b => b.Title == title).FirstOrDefaultAsync();
        }
    }
}
