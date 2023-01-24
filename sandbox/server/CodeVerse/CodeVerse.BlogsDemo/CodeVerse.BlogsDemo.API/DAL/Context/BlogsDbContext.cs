using CodeVerse.BlogsDemo.API.Models.Entity;
using Microsoft.EntityFrameworkCore;

namespace CodeVerse.BlogsDemo.API.DAL.Context
{
    public class BlogsDbContext: DbContext
    {
        public BlogsDbContext(DbContextOptions options) : base(options) { }
        
        public DbSet<Blog> Blogs { get; set; }
        public DbSet<Post> Posts { get; set; }
    }
}
