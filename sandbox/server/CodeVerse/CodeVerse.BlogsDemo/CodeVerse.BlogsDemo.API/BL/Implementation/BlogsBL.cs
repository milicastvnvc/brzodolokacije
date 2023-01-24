using CodeVerse.BlogsDemo.API.BL.Interfaces;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using CodeVerse.BlogsDemo.API.Models.Entity;

namespace CodeVerse.BlogsDemo.API.BL.Implementation
{
    public class BlogsBL : IBlogsBL
    {
        private readonly IBlogsDAL _blogsDAL;

        public BlogsBL(IBlogsDAL blogsBL)
        {
            _blogsDAL = blogsBL;
        }

        public async Task<bool> Delete(long id)
        {
            Blog? blog = await _blogsDAL.GetById(id);
            if (blog != null)
            {
                await _blogsDAL.Delete(blog);
                return true;
            }
            return false;
        }

        public async Task<Blog?> Get(long id)
        {
            return await _blogsDAL.GetById(id);
        }

        public async Task<List<Blog>> GetAll()
        {
            return await _blogsDAL.GetAll();
        }

        public async Task<bool> Insert(Blog blog)
        {
            var existingBlog = await _blogsDAL.GetBlogByTitle(blog.Title);
            if (existingBlog == null)
            {
                await _blogsDAL.Insert(blog);
                return true;
            }
            return false;
        }
    }
}
