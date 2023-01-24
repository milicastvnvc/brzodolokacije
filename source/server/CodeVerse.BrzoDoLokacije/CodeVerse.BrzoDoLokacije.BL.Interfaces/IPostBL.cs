using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.BL.Interfaces
{
    public interface IPostBL
    {
        Task<UserProfileResponse> GetPostsByUserId(long userId, int pageIndex, int pageSize);
        Task<List<Coordinate>> GetAllCoordinates();
        Task<List<PostViewModel>> GetPosts(GetPostsRequest request);
        Task<ActionResultResponse> GetPost(long postId);
        Task<ActionResultResponse> DeletePost(long postId);
        Task<ActionResultResponse> AddComment(AddCommentRequest request);
        Task<ActionResultResponse> DeleteComment(long commentId);
        Task<ActionResultResponse> UpdateComment(UpdateCommentRequest request);
        Task<ActionResultResponse> RatePost(RatePostRequest request);
        Task<ActionResultResponse> RemoveRateFromPost(long postId);
        Task<List<Coordinate>> GetUserPostsCoordinates(long userId);
        Task<List<PostViewModel>> GetPostsByCoordinates(double latitude, double longitude);
        Task<ActionResultResponse> AddImagesForPost(long postId, Dictionary<string, byte[]> images);
        Task<ActionResultResponse> CreateNewPost(AddPostRequest request);
    }
}
