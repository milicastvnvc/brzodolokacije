using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class UpdateCommentRequest
    {
        public long CommentId { get; set; }
        public string Text { get; set; }
    }
}
