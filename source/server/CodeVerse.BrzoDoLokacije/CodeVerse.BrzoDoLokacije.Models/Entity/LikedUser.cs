using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class LikedUser
    {
        public long Id { get; set; }
        [ForeignKey("FK_LikedById")]
        public long LikedById { get; set; }
        public User LikedBy { get;set; }
        [ForeignKey("FK_UserId")]
        public long UserId { get; set; }
        public User User { get; set; }
        public DateTime CreatedDate { get; set; }
    }
}
