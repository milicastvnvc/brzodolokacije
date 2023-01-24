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
    public class CommentDAL : BaseDAL<Comment>, ICommentDAL
    {
        public CommentDAL(BrzoDoLokacijeDbContext context) : base(context)
        {
        }

        public async Task<Comment?> GetComment(long id)
        {
            return await Table.Include(c => c.CreatedBy).Include(c => c.Post).Where(c => c.Id == id).FirstOrDefaultAsync();
        }

        public async Task<List<Comment>> GetCommentsForPost(long postId)
        {
           return await Table.Include(c => c.CreatedBy).Include(x => x.ChildrenComments).Where(c => c.PostId == postId && c.ParentCommentId == null).ToListAsync();
        }
    }
}
