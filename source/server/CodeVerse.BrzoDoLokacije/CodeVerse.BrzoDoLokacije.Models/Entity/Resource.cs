using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.Entity
{
    public class Resource
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Path { get; set; }
        public bool? IsPhoto { get; set; }
        [ForeignKey("FK_PostID")]
        public long PostId { get; set; }
        public Post Post { get; set; }
    }
}
