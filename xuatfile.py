import os

def export_java_to_txt(folder_path):
    if not os.path.isdir(folder_path):
        print("Đường dẫn không hợp lệ.")
        return

    folder_name = os.path.basename(os.path.normpath(folder_path))
    output_filename = f"{folder_name}.txt"
    
    java_files = [f for f in os.listdir(folder_path) if f.endswith(".java")]
    
    if not java_files:
        print("Không tìm thấy file .java nào trong thư mục.")
        return

    with open(output_filename, "w", encoding="utf-8") as outfile:
        for java_file in java_files:
            java_path = os.path.join(folder_path, java_file)
            outfile.write(f"//This is code of: {java_file}\n")
            with open(java_path, "r", encoding="utf-8") as infile:
                outfile.write(infile.read())
            outfile.write("\n")  # ngắt dòng giữa các file

    print(f"Đã tạo file: {output_filename}")

# Ví dụ sử dụng
export_java_to_txt("src\dao")
export_java_to_txt("src\connectDB")
export_java_to_txt("src\entities")
export_java_to_txt("src\gui")
export_java_to_txt("src\controller")
export_java_to_txt(r"src\utils")
