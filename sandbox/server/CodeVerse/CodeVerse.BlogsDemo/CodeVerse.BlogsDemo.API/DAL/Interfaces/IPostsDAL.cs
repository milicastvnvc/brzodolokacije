using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.DAL.Interfaces
{
    public interface IPostsDAL: IBaseDAL<Post>
    {
        Task<Post?> GetPostByTitle(string title);
        Task<List<Post>> GetPostsByBlogId(long blogId);
    }
}
