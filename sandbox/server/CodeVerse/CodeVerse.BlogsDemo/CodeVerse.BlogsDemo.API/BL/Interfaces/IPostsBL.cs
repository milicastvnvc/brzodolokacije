using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.BL.Interfaces
{
    public interface IPostsBL
    {
        Task<Post?> Get(long id);
        Task<List<Post>> GetAll();
        Task<bool> Insert(Post post);
        Task<bool> Delete(long id);
        Task<List<Post>> GetPostsByBlogId(long id);
    }
}
