namespace CodeVerse.BlogsDemo.API.DAL.Interfaces
{
    public interface IBaseDAL<T> where T : class
    {
        Task Insert(T entity);
        Task Delete(T entity);
        Task<T?> GetById(long id);
        Task<List<T>> GetAll();
        Task Update(T entity);
        Task Save();
    }
}
