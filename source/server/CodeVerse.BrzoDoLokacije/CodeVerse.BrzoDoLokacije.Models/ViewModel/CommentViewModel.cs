using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class CommentViewModel
    {
        public long Id { get; set; }
        public long CreatedById { get; set; }
        public UserViewModel CreatedBy { get; set; }
        public string Text { get; set; }
        public long PostId { get; set; }
        public DateTime CreatedDate { get; set; }
        public long? ParentCommentId { get; set; }
        public List<CommentViewModel> ChildrenComments { get; set; }
    }
}
