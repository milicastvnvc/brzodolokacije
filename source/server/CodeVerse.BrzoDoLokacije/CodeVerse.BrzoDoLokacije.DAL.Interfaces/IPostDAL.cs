using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IPostDAL : IBaseDAL<Post>
    {
        List<Post> GetPostsByUserId(long userId, int pageIndex, int pageSize, out int total);
        Task<List<Coordinate>> GetAllCoordinates();
        List<Post> GetPosts(GetPostsRequest request, out int total);
        Task<Post?> GetPost(long postId);
        Task<List<Coordinate>> GetUserPostsCoordinates(long userId);
        Task<List<Post>> GetPostsByCoordinates(double latitude, double longitude);
    }
}
