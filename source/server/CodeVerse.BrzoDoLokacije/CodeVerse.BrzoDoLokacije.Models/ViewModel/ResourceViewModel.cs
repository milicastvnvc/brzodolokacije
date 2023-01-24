﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Models.ViewModel
{
    public class ResourceViewModel
    {
        public long Id { get; set; }
        public string Name { get; set; }
        public string Path { get; set; }
        public bool IsPhoto { get; set; }
    }
}
