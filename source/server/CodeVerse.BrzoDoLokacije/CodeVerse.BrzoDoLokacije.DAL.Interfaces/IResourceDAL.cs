using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface IResourceDAL : IBaseDAL<Resource>
    {
        Task<List<Resource>> GetResourcesByPostId(long postId);
        Task DeleteResources(List<Resource> resources);
    }
}
