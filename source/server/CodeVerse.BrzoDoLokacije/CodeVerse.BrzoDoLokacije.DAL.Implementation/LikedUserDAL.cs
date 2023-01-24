using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Implementation
{
    public class LikedUserDAL : BaseDAL<LikedUser>, ILikedUserDAL
    {
        public LikedUserDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public async Task<LikedUser?> GetLikedUser(long likedById, long userId)
        {
            return await Table.Where(x => x.LikedById == likedById && x.UserId == userId).FirstOrDefaultAsync();
        }

        public async Task<List<User>> GetLikedUsers(long likedById, int pageIndex, int pageSize, string? searchTerm = null)
        {
            var query = Table.Where(x => x.LikedById == likedById).Include(x => x.User).Select(x => x.User).AsQueryable();
            
            if (!string.IsNullOrEmpty(searchTerm))
            {
                query = query.Where(x => string.Concat(x.FirstName, " ", x.LastName).Contains(searchTerm) || x.Username.Contains(searchTerm));
            }

            query = query.Skip(pageIndex).Take(pageSize);

            return await query.ToListAsync();
        }
    }
}
