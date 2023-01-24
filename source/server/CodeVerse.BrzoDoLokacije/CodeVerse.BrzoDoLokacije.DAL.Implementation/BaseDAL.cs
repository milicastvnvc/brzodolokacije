using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using CodeVerse.BrzoDoLokacije.DAL.Interfaces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Implementation
{
    public class BaseDAL<T> : IBaseDAL<T> where T : class
    {
        protected BrzoDoLokacijeDbContext Context;
        protected DbSet<T> Table;

        public BaseDAL(BrzoDoLokacijeDbContext context)
        {
            Context = context;
            Table = context.Set<T>();
        }

        public async Task AddRange(List<T> entities)
        {
            if (entities != null && entities.Count > 0)
            {
                await Table.AddRangeAsync(entities);
                await SaveChanges();
            }
        }

        public async Task Delete(T entity)
        {
            if (entity != null)
            {
                Table.Remove(entity);
                await this.SaveChanges();
            }
        }

        public async Task<T?> Get(long id)
        {
            return await Table.FindAsync(id);
        }

        public async Task<IList<T>> GetAll()
        {
            return await Table.ToListAsync();
        }

        public async Task Insert(T entity)
        {
            if (entity != null)
            {
                await Table.AddAsync(entity);
                await this.SaveChanges();
            }
        }

        public async Task SaveChanges()
        {
            await Context.SaveChangesAsync();
        }

        public async Task Update(T entity)
        {
            if (entity != null)
            {
                Table.Attach(entity);
                await this.SaveChanges();
            }
        }
    }
}
