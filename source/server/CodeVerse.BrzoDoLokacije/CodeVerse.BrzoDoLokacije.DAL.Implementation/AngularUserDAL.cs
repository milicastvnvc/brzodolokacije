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
    public class AngularUserDAL : BaseDAL<AngularUser>, IAngularUserDAL
    {
        public AngularUserDAL(BrzoDoLokacijeDbContext context) : base(context)
        {

        }

        public async Task AddApplication(byte[] application)
        {
            await Context.Applications.AddAsync(new Application { Apk = application });
            await SaveChanges();
        }

        public async Task<byte[]?> GetApplication()
        {
            return await Context.Applications.OrderByDescending(a => a.Id).Select(a => a.Apk).FirstOrDefaultAsync();
        }

        public async Task<AngularUser?> GetByUsername(string username)
        {
            return await Table.Where(u => u.Username == username).FirstOrDefaultAsync();
        }
    }
}
