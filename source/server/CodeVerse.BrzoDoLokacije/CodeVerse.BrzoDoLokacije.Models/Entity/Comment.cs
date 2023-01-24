using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class Comment
    {
        public long Id { get; set; }
        [ForeignKey("FK_CreatedBy")]
        public long CreatedById { get; set; }
        public User CreatedBy { get; set; }
        public string Text { get; set; }
        [ForeignKey("FK_PostId")]
        public long PostId { get; set; }
        public Post Post { get; set; }
        public DateTime CreatedDate { get; set; }
        [ForeignKey("FK_ParrentComment")]
        public long? ParentCommentId { get; set; }
        public Comment ParentComment { get; set; }
        public virtual List<Comment> ChildrenComments { get; set; }
    }
}
