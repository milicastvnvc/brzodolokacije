using CodeVerse.BrzoDoLokacije.Models.Entity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class UserAvgRating
    {
        public long UserId { get; set; }
        public List<int> Ratings { get; set; } = new List<int>();
    }
}
