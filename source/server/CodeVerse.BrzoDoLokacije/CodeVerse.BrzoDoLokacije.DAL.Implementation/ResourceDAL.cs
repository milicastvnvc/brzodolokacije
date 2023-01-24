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
    public class ResourceDAL : BaseDAL<Resource>, IResourceDAL
    {
        public ResourceDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public async Task DeleteResources(List<Resource> resources)
        {
            Table.RemoveRange(resources);
            await SaveChanges();
        }

        public async Task<List<Resource>> GetResourcesByPostId(long postId)
        {
            return await Table.Where(x => x.PostId == postId).ToListAsync();
        }
    }
}
