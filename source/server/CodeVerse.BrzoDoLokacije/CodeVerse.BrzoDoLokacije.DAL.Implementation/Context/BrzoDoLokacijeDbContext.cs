using CodeVerse.BrzoDoLokacije.Models.Entity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.DAL.Implementation.Context
{
    public class BrzoDoLokacijeDbContext : DbContext
    {
        public BrzoDoLokacijeDbContext(DbContextOptions<BrzoDoLokacijeDbContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Post> Posts { get; set; }
        public DbSet<Resource> Resources { get; set; }
        public DbSet<AngularUser> AngularUsers { get; set; }
        public DbSet<Application> Applications { get; set; }
        public DbSet<Comment> Comments { get; set; }
        public DbSet<Rating> Ratings { get; set; }
        public DbSet<LikedUser> LikedUsers { get; set; }
    }
}
