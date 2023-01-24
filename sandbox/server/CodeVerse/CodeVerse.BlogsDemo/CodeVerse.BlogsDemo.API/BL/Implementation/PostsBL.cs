using CodeVerse.BlogsDemo.API.BL.Interfaces;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.BL.Implementation
{
    public class PostsBL : IPostsBL
    {
        private readonly IPostsDAL _postsDAL;

        public PostsBL(IPostsDAL postsDAL)
        {
            _postsDAL = postsDAL;
        }

        public async Task<bool> Delete(long id)
        {
            Post? post = await _postsDAL.GetById(id);
            if (post != null)
            {
                await _postsDAL.Delete(post);
                return true;
            }
            return false;
        }

        public async Task<Post?> Get(long id)
        {
            return await _postsDAL.GetById(id);
        }

        public async Task<List<Post>> GetAll()
        {
            return await _postsDAL.GetAll();
        }

        public async Task<List<Post>> GetPostsByBlogId(long id)
        {
            return await _postsDAL.GetPostsByBlogId(id);
        }

        public async Task<bool> Insert(Post post)
        {
            post.Blog = null;
            var existingPost = await _postsDAL.GetPostByTitle(post.Title);
            if (existingPost == null)
            {
                await _postsDAL.Insert(post);
                return true;
            }
            return false;
        }
    }
}
