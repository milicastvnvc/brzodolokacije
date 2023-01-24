using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Internal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Implementation
{
    public class PostDAL : BaseDAL<Post>, IPostDAL
    {
        public PostDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public async Task<List<Coordinate>> GetAllCoordinates()
        {
            return await Table.Select(p => new Coordinate
            {
                PostId = p.Id,
                Longitude = double.Parse(p.LongitudeCenter),
                Latitude = double.Parse(p.LatitudeCenter)
            }).Distinct().ToListAsync();
        }

        public async Task<Post?> GetPost(long postId)
        {
            return await Table
                .Include(p => p.CreatedBy)
                .Include(p => p.Resources)
                .Where(p => p.Id == postId)
                .FirstOrDefaultAsync();
        }

        public List<Post> GetPosts(GetPostsRequest request, out int total)
        {
            var query = Table
                .Include(p => p.Resources)
                .Include(p => p.CreatedBy)
                .Include(p => p.Ratings)
                .Include(p => p.Comments)
                .AsQueryable();

            if (request.Latitude != null && request.Longitude != null)
            {
                query = query.Where(p => p.LongitudeCenter == request.Longitude.ToString() && 
                p.LatitudeCenter == request.Latitude.ToString());
            }

            if (request.PlaceName != null)
            {
                query = query.Where(p => p.PostSearchTerm != null && p.PostSearchTerm.Contains(request.PlaceName.ToUpper().Trim()));
            }

            if (request.Tags != null)
            {
                foreach (var tag in request.Tags)
                {
                    query = query.Where(p => !string.IsNullOrEmpty(p.Tags) && p.Tags.Contains(tag.ToLower().Trim()));
                }
            }

            switch(request.SortBy)
            {
                case PostSortBy.CreatedDate:
                    if (request.SortOrder == SortOrder.Descending)
                    {
                        query = query.OrderByDescending(p => p.DateCreated);
                    }
                    else
                    {
                        query = query.OrderBy(p => p.DateCreated);
                    }
                    break;
                case PostSortBy.Rating:
                    if (request.SortOrder == SortOrder.Descending)
                    {
                        query = query.OrderByDescending(p => p.Ratings.Select(x => x.Rate).Average());
                    }
                    else
                    {
                        query = query.OrderBy(p => p.Ratings.Select(x => x.Rate).Average());
                    }
                    break;
                case PostSortBy.NumberOfComments:
                    if (request.SortOrder == SortOrder.Descending)
                    {
                        query = query.OrderByDescending(p => p.Comments.Count);
                    }
                    else
                    {
                        query = query.OrderBy(p => p.Comments.Count);
                    }
                    break;
                default: break;
            }

            total = query.Count();
            query = query.Skip(request.PageIndex);
            query = query.Take(request.PageSize);

            return query.ToList();
        }

        public async Task<List<Post>> GetPostsByCoordinates(double latitude, double longitude)
        {
            return await Table
                .Where(p => p.LatitudeCenter == latitude.ToString() && p.LongitudeCenter == longitude.ToString())
                .ToListAsync();
        }

        public List<Post> GetPostsByUserId(long userId, int pageIndex, int pageSize, out int total)
        {
            var query = Table.Where(p => p.CreatedById == userId).AsQueryable();
            total = query.Count();
            query = query
                .OrderByDescending(p => p.DateCreated)
                .Include(p => p.CreatedBy)
                .Include(p => p.Resources)
                .Skip(pageIndex)
                .Take(pageSize);

            return query.ToList();
        }

        public async Task<List<Coordinate>> GetUserPostsCoordinates(long userId)
        {
            return await Table.Where(p => p.CreatedById == userId)
                .Select(x => new Coordinate()
                {
                    PostId = x.Id,
                    Latitude = double.Parse(x.LatitudeCenter),
                    Longitude = double.Parse(x.LongitudeCenter)
                }).ToListAsync();
        }
    }
}
