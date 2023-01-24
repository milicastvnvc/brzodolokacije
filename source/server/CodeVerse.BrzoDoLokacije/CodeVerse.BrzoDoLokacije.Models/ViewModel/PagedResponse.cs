using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class PagedResponse<T> where T : class
    {
        public List<T> Items { get; set; }
        public int Total { get; set; }
    }
}
