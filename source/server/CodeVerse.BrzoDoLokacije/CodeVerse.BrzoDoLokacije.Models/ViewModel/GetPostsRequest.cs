using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class GetPostsRequest
    {
        public double? Longitude { get; set; } = null;
        public double? Latitude { get; set; } = null;
        public string? PlaceName { get; set; }
        public List<string>? Tags { get; set; }
        public PostSortBy SortBy { get; set; } = PostSortBy.CreatedDate;
        public SortOrder SortOrder { get; set; } = SortOrder.Descending;
        public int PageIndex { get; set; } = 0;
        public int PageSize { get; set; } = 20;
    }
}
