using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace CodeVerse.BrzoDoLokacije.API.Migrations
{
    public partial class PostChanges2 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<string>(
                name: "PostSearchTerm",
                table: "Posts",
                type: "TEXT",
                nullable: true,
                defaultValue: null,
                oldClrType: typeof(string),
                oldType: "TEXT");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<string>(
                name: "PostSearchTerm",
                table: "Posts",
                type: "TEXT",
                nullable: false,
                defaultValue: "",
                oldClrType: typeof(string),
                oldType: "TEXT",
                oldNullable: true);
        }
    }
}
