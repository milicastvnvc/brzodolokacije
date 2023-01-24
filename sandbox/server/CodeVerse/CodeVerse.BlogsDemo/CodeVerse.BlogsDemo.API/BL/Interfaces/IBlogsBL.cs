using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.BL.Interfaces
{
    public interface IBlogsBL
    {
        Task<Blog?> Get(long id);
        Task<List<Blog>> GetAll();
        Task<bool> Delete(long id);
        Task<bool> Insert(Blog blog);
    }
}
