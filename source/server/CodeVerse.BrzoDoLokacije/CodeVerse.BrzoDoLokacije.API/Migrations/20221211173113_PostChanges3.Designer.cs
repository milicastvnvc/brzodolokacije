// <auto-generated />
using System;
using CodeVerse.BrzoDoLokacije.DAL.Implementation.Context;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

#nullable disable

namespace CodeVerse.BrzoDoLokacije.API.Migrations
{
    [DbContext(typeof(BrzoDoLokacijeDbContext))]
    [Migration("20221211173113_PostChanges3")]
    partial class PostChanges3
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder.HasAnnotation("ProductVersion", "6.0.10");

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.AngularUser", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.HasKey("Id");

                    b.ToTable("AngularUsers");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Application", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<byte[]>("Apk")
                        .IsRequired()
                        .HasColumnType("BLOB");

                    b.HasKey("Id");

                    b.ToTable("Applications");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Comment", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<long>("CreatedById")
                        .HasColumnType("INTEGER");

                    b.Property<DateTime>("CreatedDate")
                        .HasColumnType("TEXT");

                    b.Property<long?>("ParentCommentId")
                        .HasColumnType("INTEGER");

                    b.Property<long>("PostId")
                        .HasColumnType("INTEGER");

                    b.Property<string>("Text")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.HasKey("Id");

                    b.HasIndex("CreatedById");

                    b.HasIndex("ParentCommentId");

                    b.HasIndex("PostId");

                    b.ToTable("Comments");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.LikedUser", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<DateTime>("CreatedDate")
                        .HasColumnType("TEXT");

                    b.Property<long>("LikedById")
                        .HasColumnType("INTEGER");

                    b.Property<long>("UserId")
                        .HasColumnType("INTEGER");

                    b.HasKey("Id");

                    b.HasIndex("LikedById");

                    b.HasIndex("UserId");

                    b.ToTable("LikedUsers");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Post", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<long>("CreatedById")
                        .HasColumnType("INTEGER");

                    b.Property<DateTime>("DateCreated")
                        .HasColumnType("TEXT");

                    b.Property<string>("Description")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("LatitudeCenter")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("LongitudeCenter")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("PlaceName")
                        .HasColumnType("TEXT");

                    b.Property<string>("PlaceNameSr")
                        .HasColumnType("TEXT");

                    b.Property<string>("PostSearchTerm")
                        .HasColumnType("TEXT");

                    b.Property<string>("Tags")
                        .HasColumnType("TEXT");

                    b.Property<string>("Text")
                        .HasColumnType("TEXT");

                    b.HasKey("Id");

                    b.HasIndex("CreatedById");

                    b.ToTable("Posts");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Rating", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<DateTime>("CreatedDate")
                        .HasColumnType("TEXT");

                    b.Property<long>("PostId")
                        .HasColumnType("INTEGER");

                    b.Property<int>("Rate")
                        .HasColumnType("INTEGER");

                    b.Property<long>("UserId")
                        .HasColumnType("INTEGER");

                    b.HasKey("Id");

                    b.HasIndex("PostId");

                    b.HasIndex("UserId");

                    b.ToTable("Ratings");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Resource", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<bool?>("IsPhoto")
                        .HasColumnType("INTEGER");

                    b.Property<string>("Name")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("Path")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<long>("PostId")
                        .HasColumnType("INTEGER");

                    b.HasKey("Id");

                    b.HasIndex("PostId");

                    b.ToTable("Resources");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.User", b =>
                {
                    b.Property<long>("Id")
                        .ValueGeneratedOnAdd()
                        .HasColumnType("INTEGER");

                    b.Property<string>("Email")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("FirstName")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<bool?>("IsVerified")
                        .HasColumnType("INTEGER");

                    b.Property<string>("LastName")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("Password")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.Property<string>("ProfilePicturePath")
                        .HasColumnType("TEXT");

                    b.Property<string>("RefreshToken")
                        .HasColumnType("TEXT");

                    b.Property<DateTime?>("RefreshTokenCreated")
                        .HasColumnType("TEXT");

                    b.Property<DateTime?>("RefreshTokenExpires")
                        .HasColumnType("TEXT");

                    b.Property<string>("Token")
                        .HasColumnType("TEXT");

                    b.Property<DateTime?>("TokenExpiration")
                        .HasColumnType("TEXT");

                    b.Property<string>("Username")
                        .IsRequired()
                        .HasColumnType("TEXT");

                    b.HasKey("Id");

                    b.ToTable("Users");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Comment", b =>
                {
                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.User", "CreatedBy")
                        .WithMany("Comments")
                        .HasForeignKey("CreatedById")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.Comment", "ParentComment")
                        .WithMany("ChildrenComments")
                        .HasForeignKey("ParentCommentId");

                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.Post", "Post")
                        .WithMany("Comments")
                        .HasForeignKey("PostId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("CreatedBy");

                    b.Navigation("ParentComment");

                    b.Navigation("Post");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.LikedUser", b =>
                {
                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.User", "LikedBy")
                        .WithMany("Users")
                        .HasForeignKey("LikedById")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.User", "User")
                        .WithMany("LikedUsers")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("LikedBy");

                    b.Navigation("User");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Post", b =>
                {
                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.User", "CreatedBy")
                        .WithMany("Posts")
                        .HasForeignKey("CreatedById")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("CreatedBy");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Rating", b =>
                {
                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.Post", "Post")
                        .WithMany("Ratings")
                        .HasForeignKey("PostId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.User", "User")
                        .WithMany("Ratings")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Post");

                    b.Navigation("User");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Resource", b =>
                {
                    b.HasOne("CodeVerse.BrzoDoLokacije.Models.Entity.Post", "Post")
                        .WithMany("Resources")
                        .HasForeignKey("PostId")
                        .OnDelete(DeleteBehavior.Cascade)
                        .IsRequired();

                    b.Navigation("Post");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Comment", b =>
                {
                    b.Navigation("ChildrenComments");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.Post", b =>
                {
                    b.Navigation("Comments");

                    b.Navigation("Ratings");

                    b.Navigation("Resources");
                });

            modelBuilder.Entity("CodeVerse.BrzoDoLokacije.Models.Entity.User", b =>
                {
                    b.Navigation("Comments");

                    b.Navigation("LikedUsers");

                    b.Navigation("Posts");

                    b.Navigation("Ratings");

                    b.Navigation("Users");
                });
#pragma warning restore 612, 618
        }
    }
}
