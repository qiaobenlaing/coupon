//
//  BMSQ_EditPhotoProduct.m
//  BMSQS
//
//  Created by gh on 15/10/19.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_EditPhotoViewController.h"
#import "UIImageView+WebCache.h"

#import "BMSQ_SelectSubAlbumViewController.h"
#import "AFURLSessionManager.h"
#import "DAProgressOverlayView.h"
#import "VPImageCropperViewController.h"
#import "AFURLResponseSerialization.h"
#import "AFURLSessionManager.h"

#import "UIImageView+WebCache.h"

@interface BMSQ_EditPhotoViewController() {
    
    
    
    NSArray *array_subAlbum;
    
    UITableView *m_tableView;
    
    UITextField *textName;
    UITextField *text_price;
    
    UILabel *lb_albumName;
    
    //产品
    NSString *subAlbumCode_edit; //子相册编码
    NSString *url_edit;          //图片url
    NSString *code_edit;
    
    //环境
    NSString *decorationCode_edit;
    
    UITextField *text_Title;
 
    UIImageView *img;
    
    
    
    
}

@end

@implementation BMSQ_EditPhotoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    [self setViewUp];
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
    
}

- (void)setViewUp {
    
    [self setNavBackItem];
    [self customRightBtn];

    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT+49)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    [self.view addSubview:m_tableView];
    
    
    img = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 70, 70)];
    img.backgroundColor = [UIColor redColor];
    img.image = self.image;
    
    lb_albumName = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, 44)];
    lb_albumName.font = [UIFont systemFontOfSize:13];
    //    lb_albumName.text = [NSString stringWithFormat:@"%@",[self.dic objectForKey:@"name"]];
    lb_albumName.backgroundColor = [UIColor clearColor];
    lb_albumName.text = self.productName;
    
    textName = [[UITextField alloc] initWithFrame:CGRectMake(10, 2, APP_VIEW_WIDTH-20, 40)];
    //设置输入框的背景颜色，此时设置为白色如果使用了自定义的背景图片边框会被忽略掉
    textName.backgroundColor = [UIColor whiteColor];
    textName.placeholder = @"请输入产品的名字";
    
    //产品价格
    text_price = [[UITextField alloc] initWithFrame:CGRectMake(60, 2, 100, 40)];
    text_price.backgroundColor = [UIColor whiteColor];
    text_price.keyboardType = UIKeyboardTypeDecimalPad;
    
    
    
    text_Title = [[UITextField alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
    text_Title.backgroundColor = [UIColor whiteColor];
    text_Title.placeholder = @"请输入照片标题";

    if (_editDic) {
        if (_isProduct) {
            url_edit = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"url"]];
            code_edit = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"code"]];
            subAlbumCode_edit = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"subAlbumCode"]];
            lb_albumName.text = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"subAlbumName"]];
            textName.text = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"title"]];
            text_price.text = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"price"]];
            
            [img setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, url_edit]]];
            
        }else if (!_isProduct) {
            
            NSLog(@"%@",_editDic);
            url_edit = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"imgUrl"]];
            decorationCode_edit = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"decorationCode"]];
            text_Title.text = [NSString stringWithFormat:@"%@",[_editDic objectForKey:@"title"]];
             [img setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL, url_edit]]];
            
        }
        
        
        
        
    }else {
        img.image = self.image;
    }
    
}

//右边的按钮
- (void)customRightBtn {
    UIButton* btnRight = [UIButton buttonWithType:UIButtonTypeCustom];
    btnRight.frame = CGRectMake(APP_VIEW_WIDTH-44, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    [btnRight setTitle:@"完成" forState:UIControlStateNormal];
    [btnRight addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    btnRight.tag = 901;
    [self.view addSubview:btnRight];
    
}


//退出按钮
- (void)setNavBackItem {
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    btnback.tag = 902;
    [btnback addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnback];

}


- (void)btnAction:(UIButton *)button {
    
    if (button.tag == 901) { //完成
        
        if (_isProduct){
            
            if (textName.text.length <=0) {
                CSAlert(@"请输入产品的名称");
                return;
                
            }
            if (text_price.text.length <= 0) {
                CSAlert(@"请输入产品的价格");
                return;
            }
            if (_editDic) {
                [self updateSubAlbumPhoto];
            }else {
                [self upLoadimg];
            }
            
          
        }else if (!_isProduct) {
            if (text_Title.text.length <=0) {
                CSAlert(@"请输入照片的标题");
                return;
            }
            if (_editDic) {
                [self updateShopDecoration];
            }else {
                [self upLoadimg];
            }
            
        }
        
        
    }else if (button.tag == 902) {//退出 返回
        [self dismissViewControllerAnimated:YES completion:^{
        
        }];
    }
    
}


#pragma mark - UITableView delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (_isProduct) {
        
        return 5;
    }else if(!_isProduct) {
        return 3;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    
    if (_isProduct) {
        
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (indexPath.row == 0) {
           
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 89.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            [cell addSubview:img];
            
        }else if (indexPath.row == 1) {
            UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
            lb_left.backgroundColor = [UIColor clearColor];
            lb_left.font = [UIFont systemFontOfSize:13];
            lb_left.text = @"上传到 ：";
            [cell addSubview:lb_left];
            
            [cell addSubview:lb_albumName];
            
            UIImageView *iv_jtRight = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 17, 5, 10)];
            iv_jtRight.image = [UIImage imageNamed:@"iv_jtRight"];
            [cell addSubview:iv_jtRight];
            
            UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 43.5, APP_VIEW_WIDTH, APP_CELL_LINE_HEIGHT)];
            line.backgroundColor = APP_CELL_LINE_COLOR;
            [cell addSubview:line];
            
            
        }else if (indexPath.row == 2) {
            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 44)];
            label.font = [UIFont systemFontOfSize:13];
            label.text = @"产品名字 ：";
            [cell addSubview:label];
            
            
        }else if (indexPath.row == 3) {
            
            [cell addSubview:textName];
            
            
        }else if (indexPath.row == 4) {
            UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, 50, 44)];
            label.text = @"价格:";
            [cell addSubview:label];
            
            
            [cell addSubview:text_price];
            
            UILabel *label2 = [[UILabel alloc] initWithFrame:CGRectMake(text_price.frame.origin.x+text_price.frame.size.width, 0, 50, 44)];
            label2.backgroundColor = [UIColor clearColor];
            label2.text = @"元";
            [cell addSubview:label2];
            
            
        }
        
        return cell;
        
    }else if(!_isProduct) {
        
        UITableViewCell *cell = [[UITableViewCell alloc] init];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        
        if (indexPath.row == 0) {
            [cell addSubview:img];
            
        }else if (indexPath.row == 1) {
            UILabel *lb_left = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH, 44)];
            lb_left.font = [UIFont systemFontOfSize:13];
            lb_left.text = @"照片标题:";
            [cell addSubview:lb_left];
            
        }else if (indexPath.row == 2) {
            
            [cell addSubview:text_Title];
        }
        
        
        
        return cell;
    }
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (_isProduct && indexPath.row == 1) {
        
        NSLog(@"选择类别");
        
        BMSQ_SelectSubAlbumViewController *selectVC = [[BMSQ_SelectSubAlbumViewController alloc] init];
        
        [self presentViewController:selectVC animated:YES completion:^{
            selectVC.delegate = self;
        }];
        
    }

}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (_isProduct) {
        if (indexPath.row == 0) {
            return 90;
        }
        return 44;
        
    }else if (!_isProduct) {
        if (indexPath.row == 0) {
            return 90;
        }
        return 44;
        
        
    }
    return 0;
}



- (void)setAlbumPhotoDic:(NSDictionary *)dictionary { //选择的相册

//    NSLog(@"%@",dictionary);
    
    self.dic = dictionary;
    lb_albumName.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"name"]];
    
    [m_tableView reloadData];
    
}

- (void)upLoadimg {
    
    [SVProgressHUD showWithStatus:@"正在上传图片"];
    
    NSData *dataObj = UIImageJPEGRepresentation(_image, 0.5);
    
    if([gloabFunction getShopCode].length==0||[gloabFunction getShopCode]==nil)
        return;
    
    NSMutableURLRequest *request = [[AFHTTPRequestSerializer serializer] multipartFormRequestWithMethod:@"POST" URLString:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL ] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:dataObj name:@"imagefile" fileName:@"Icon.png" mimeType:@"image/png"];

        
    } error:nil];
    
    NSProgress *progressData = [NSProgress progressWithTotalUnitCount:dataObj.length];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
    AFHTTPResponseSerializer *aa= [AFJSONResponseSerializer serializer];
    aa.acceptableContentTypes = [NSSet setWithObjects:@"application/json",@"text/json",@"text/javascript",@"text/html",@"text/plain",nil];
    manager.responseSerializer = aa;
    
    NSURLSessionUploadTask *uploadTask = [manager uploadTaskWithStreamedRequest:request progress:&progressData completionHandler:^(NSURLResponse *response, id responseObject, NSError *error) {
        if (error) {
            NSString *aString = [[NSString alloc] initWithData:[error.userInfo objectForKey:@"com.alamofire.serialization.response.error.data"] encoding:NSUTF8StringEncoding];
            NSLog(@"Error: %@",aString );
            [SVProgressHUD dismiss];
            
        }else {
            NSLog(@"%@",responseObject);
            NSString *code = [responseObject objectForKey:@"code"];
            if (code.length>5){
                url_edit = code;
                if (!_isProduct) {
                    [self addShopDecImg];
                }
                else if (_isProduct) {
                    [self addSubAlbumPhoto];
                    
                }
                
                
            }else if ([code isEqualToString:@"80020"]) {
                CSAlert(@"图片格式不正确");
            }else if ([code isEqualToString:@"80021"]) {
                CSAlert(@"图片大小不正确");
            }
            [SVProgressHUD dismiss];
        }
    }];
    
    [uploadTask resume];
    

}

//添加环境图片
- (void)addShopDecImg {
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"正在上传"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:url_edit forKeyedSubscript:@"imgUrl"];
    [params setObject:text_Title.text forKeyedSubscript:@"title"];
    NSString* vcode = [gloabFunction getSign:@"addShopDecImg" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"addShopDecImg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"上传成功"];
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
            
        }else if (code.intValue == 20000) {
            [SVProgressHUD showErrorWithStatus:@"失败，请重试"];
            
        }else if (code.intValue == 50600) {
            [SVProgressHUD showErrorWithStatus:@"装饰图片不正确"];
            
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
        [SVProgressHUD showErrorWithStatus:@"上传失败"];
        
    }];
    
}

//添加产品相册的图片
- (void)addSubAlbumPhoto {
    
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"正在上传"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[_dic objectForKey:@"code"] forKey:@"subAlbumCode"];
    [params setObject:url_edit forKeyedSubscript:@"url"];
    
    [params setObject:textName.text forKey:@"title"];
    [params setObject:text_price.text forKey:@"price"];
    [params setObject:@"" forKey:@"des"];
    
    NSString* vcode = [gloabFunction getSign:@"addSubAlbumPhoto" strParams:[_dic objectForKey:@"code"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"addSubAlbumPhoto" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"上传成功"];
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
            
        }else if (code.intValue == 20000) {
            [SVProgressHUD showErrorWithStatus:@"失败，请重试"];
            
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
        [SVProgressHUD showErrorWithStatus:@"上传失败"];
        
    }];
    
}

//修改子相册图片
- (void)updateSubAlbumPhoto {
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"请稍等..."];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    NSString *subAlbumCodeString;
    if (self.dic) {
        subAlbumCodeString =[_dic objectForKey:@"code"];
        
    }else {
        subAlbumCodeString = subAlbumCode_edit;
        
    }
    [params setObject:subAlbumCodeString forKey:@"subAlbumCode"];
    [params setObject:url_edit forKeyedSubscript:@"url"];
    [params setObject:textName.text forKey:@"title"];
    [params setObject:text_price.text forKey:@"price"];
    [params setObject:@"" forKey:@"des"];
    [params setObject:code_edit forKey:@"code"];
    
    NSString* vcode = [gloabFunction getSign:@"updateSubAlbumPhoto" strParams:subAlbumCodeString];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"updateSubAlbumPhoto" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"修改成功"];
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
            
        }else if (code.intValue == 20000) {
            [SVProgressHUD showErrorWithStatus:@"失败，请重试"];
            
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
        [SVProgressHUD showErrorWithStatus:@"上传失败"];
        
    }];

}
//修改商铺装修图片
- (void)updateShopDecoration {
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@"正在上传"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:decorationCode_edit forKey:@"decorationCode"];
    [params setObject:url_edit forKeyedSubscript:@"imgUrl"];
    [params setObject:text_Title.text forKeyedSubscript:@"title"];
    NSString* vcode = [gloabFunction getSign:@"updateShopDecoration" strParams:decorationCode_edit];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"updateShopDecoration" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSString *code =  [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if (code.intValue == 50000) {
            [SVProgressHUD showSuccessWithStatus:@"上传成功"];
            [self dismissViewControllerAnimated:YES completion:^{
                
            }];
            
        }else if (code.intValue == 20000) {
            [SVProgressHUD showErrorWithStatus:@"失败，请重试"];
            
        }else if (code.intValue == 50600) {
            [SVProgressHUD showErrorWithStatus:@"装饰图片不正确"];
            
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"请求错误");
        [SVProgressHUD showErrorWithStatus:@"上传失败"];
        
    }];
    
    
}



@end
