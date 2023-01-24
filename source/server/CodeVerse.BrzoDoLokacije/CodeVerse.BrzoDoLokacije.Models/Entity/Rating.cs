using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class Rating
    {
        public long Id { get; set; }
        public int Rate { get; set; }
        [ForeignKey("FK_PostId")]
        public long PostId { get; set; }
        public Post Post { get; set; }
        [ForeignKey("FK_UserId")]
        public long UserId { get; set; }
        public User User { get; set; }
        public DateTime CreatedDate { get; set; }
    }
}
