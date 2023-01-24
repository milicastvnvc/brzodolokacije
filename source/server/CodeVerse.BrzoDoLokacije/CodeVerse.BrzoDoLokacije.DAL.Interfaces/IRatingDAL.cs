using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IRatingDAL : IBaseDAL<Rating>
    {
        Task<Rating?> GetRating(long postId, long userId);
        double GetAvgRatingForUser(long userId, out int numberOfRatings);
        double GetAvgRatingForPost(long postId, out int numberOfRatings);
        List<AvgPostRating> GetAvgRatingForPosts(List<long> posts);
        List<UserAvgRating> GetAvgRatingForUsers(List<long> users);
    }
}
