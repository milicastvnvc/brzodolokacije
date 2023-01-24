using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IAngularUserDAL : IBaseDAL<AngularUser>
    {
        Task<AngularUser?> GetByUsername(string username);
        Task<byte[]?> GetApplication();
        Task AddApplication(byte[] application);
    }
}
