using CodeVerse.BlogsDemo.API.DAL.Context;
using CodeVerse.BlogsDemo.API.DAL.Interfaces;
using Microsoft.EntityFrameworkCore;

namespace CodeVerse.BlogsDemo.API.DAL.Implementation
{
    public class BaseDAL<T> : IBaseDAL<T> where T : class
    {
        protected BlogsDbContext Context;
        protected DbSet<T> Table;

        public BaseDAL(BlogsDbContext context)
        {
            Context = context;
            Table = context.Set<T>();
        }

        public async Task Delete(T entity)
        {
            if (entity != null)
            {
                Table.Remove(entity);
                await Save();
            }
        }

        public async Task<List<T>> GetAll()
        {
            return await Table.ToListAsync();
        }

        public async Task<T?> GetById(long id)
        {
            return await Table.FindAsync(id);
        }

        public async Task Insert(T entity)
        {
            if (entity != null)
            {
                await Table.AddAsync(entity);
                Save();
            }
        }

        public async Task Save()
        {
            await Context.SaveChangesAsync();
        }

        public async Task Update(T entity)
        {
            if (entity != null)
            {
                Table.Attach(entity);
                Context.Entry(entity).State = EntityState.Modified;
                Save();
            }
        }
    }
}
