using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class AddPostRequest
    {
        public string? Description { get; set; }
        [Required]
        public double LongitudeCenter { get; set; }
        [Required]
        public double LatitudeCenter { get; set; }
        [Required]
        public string? PlaceName { get; set; }
        public string? Text { get; set; }
        public string? PlaceNameSr { get; set; }
        [Required]
        public List<string> Tags { get; set; }
    }
}
