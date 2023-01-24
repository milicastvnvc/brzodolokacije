using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Interfaces
{
    public interface ICommentDAL : IBaseDAL<Comment>
    {
        Task<Comment?> GetComment(long id);
        Task<List<Comment>> GetCommentsForPost(long postId);
    }
}
