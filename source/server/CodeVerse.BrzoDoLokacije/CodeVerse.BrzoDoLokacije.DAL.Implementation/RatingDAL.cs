using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Implementation
{
    public class RatingDAL : BaseDAL<Rating>, IRatingDAL 
    {
        public RatingDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public List<UserAvgRating> GetAvgRatingForUsers(List<long> users)
        {
            var query = Context.Posts.Include(x => x.Ratings).Where(x => users.Contains(x.CreatedById) && x.Ratings.Count > 0);

            return query.Select(x => new UserAvgRating
            {
                UserId = x.CreatedById,
                Ratings = x.Ratings.Select(x => x.Rate).ToList()
            }).ToList();
        }

        public double GetAvgRatingForPost(long postId, out int numberOfRatings)
        {
            var query = Table.Where(r => r.PostId == postId);
            numberOfRatings = query.Count();
            return numberOfRatings > 0 ? query.Average(r => r.Rate) : 0;
        }

        public List<AvgPostRating> GetAvgRatingForPosts(List<long> posts)
        {
            return Table.Where(r => posts.Contains(r.PostId))
                .GroupBy(g => g.PostId, c => c.Rate)
                .Select(s => new AvgPostRating { PostId = s.Key, AvgRating = s.Count() > 0 ? s.Average() : 0 }).ToList();
        }

        public double GetAvgRatingForUser(long userId, out int numberOfRatings)
        {
            var query = Table.Include(r => r.Post).Where(r => r.Post.CreatedById == userId);
            numberOfRatings = query.Count();
            return numberOfRatings > 0 ? query.Average(r => r.Rate) : 0;
        }

        public async Task<Rating?> GetRating(long postId, long userId)
        {
            return await Table.Where(r => r.PostId == postId && r.UserId == userId).FirstOrDefaultAsync();
        }
    }
}
