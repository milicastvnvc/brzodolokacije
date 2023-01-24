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
    public class UserDAL : BaseDAL<User>, IUserDAL
    {
        public UserDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public Task<User?> GetByEmail(string email)
        {
            return Table.Where(u => u.Email == email).FirstOrDefaultAsync();
        }

        public Task<User?> GetByUsername(string username)
        {
            return Table.Where(u => u.Username.ToLower().Trim() == username.ToLower().Trim()).FirstOrDefaultAsync();
        }

        public Task<User?> GetByUsernameAndPassword(string username, string password)
        {
            return Table.Where(u => u.Username.ToLower().Trim() == username.ToLower().Trim() && u.Password == password).FirstOrDefaultAsync();
        }

        public Task<User?> GetUserByVerificationToken(string token)
        {
            return Table.Where(u => u.Token != null && u.Token.ToLower().Trim() == token.ToLower().Trim()).FirstOrDefaultAsync();
        }
    }
}
