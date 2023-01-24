using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IBaseDAL<T> where T : class
    {
        Task<IList<T>> GetAll();
        Task<T?> Get(long id);
        Task Update(T entity);
        Task Delete(T entity);
        Task Insert(T entity);
        Task SaveChanges();
        Task AddRange(List<T> entities);
    }
}
