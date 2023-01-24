using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IUserDAL : IBaseDAL<User>
    {
        Task<User?> GetByUsername(string username);
        Task<User?> GetByEmail(string email);
        Task<User?> GetByUsernameAndPassword(string username, string password);
        Task<User?> GetUserByVerificationToken(string token);
    }
}
