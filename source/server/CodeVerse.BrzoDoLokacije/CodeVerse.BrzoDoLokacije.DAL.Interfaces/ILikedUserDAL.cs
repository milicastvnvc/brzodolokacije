using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface ILikedUserDAL : IBaseDAL<LikedUser>
    {
        Task<LikedUser?> GetLikedUser(long likedById, long userId);
        Task<List<User>> GetLikedUsers(long likedById, int pageIndex, int pageSize, string? searchTerm = null);
    }
}
