using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.DAL.Interfaces
{
    public interface IBlogsDAL : IBaseDAL<Blog>
    {
        Task<Blog?> GetBlogByTitle(string name);
    }
}
