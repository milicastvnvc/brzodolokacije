using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ImageRecognition
{
    public class ImageRecognitionResponse
    {
        public string Picture { get; set; }
        public bool HasFace { get; set; }
    }
}
