using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace CodeVerse.BrzoDoLokacije.API.Migrations
{
    public partial class PostChanges : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "PostSearchTerm",
                table: "Posts",
                type: "TEXT",
                nullable: false,
                defaultValue: "");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "PostSearchTerm",
                table: "Posts");
        }
    }
}
