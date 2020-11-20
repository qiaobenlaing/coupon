//
//  BMSQ_SelectSubAlbumViewController.m
//  BMSQS
//
//  Created by gh on 15/10/20.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_SelectSubAlbumViewController.h"
#import "SVProgressHUD.h"

@interface BMSQ_SelectSubAlbumViewController () {
    
    UITableView *m_tableView;
    
}

@property(nonatomic,strong)NSArray* array_subAlbumList;


@end


@implementation BMSQ_SelectSubAlbumViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setViewUp];
    
    [self.view setBackgroundColor:APP_VIEW_BACKCOLOR];
}

- (void)setViewUp {
    
    [self setNavBackItem];
    

    UIImageView *image = [[UIImageView alloc]initWithFrame:CGRectMake(10, 100, 100, 100) ];
    image.backgroundColor = [UIColor redColor];
    [self.view addSubview:image];
    
    m_tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT+44)];
    m_tableView.dataSource = self;
    m_tableView.delegate = self;
    m_tableView.backgroundColor = APP_VIEW_BACKCOLOR;
    
    [self.view addSubview:m_tableView];
    
    
    [self listSubAlbum];
}


- (void)setNavBackItem {
    UIButton* btnback = [UIButton buttonWithType:UIButtonTypeCustom];
    btnback.frame = CGRectMake(0, (44-APP_NAV_LEFT_ITEM_HEIGHT)/2 + (APP_STATUSBAR_HEIGHT ), 44, APP_NAV_LEFT_ITEM_HEIGHT);
    UIImageView* btnBackView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 7, 30, 30)];
    btnBackView.image = [UIImage imageNamed:@"btn_backNormal"];
    [btnback addSubview:btnBackView];
    [btnback addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnback];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if ([self.array_subAlbumList count]) {
        return self.array_subAlbumList.count;
    }
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if ([self.array_subAlbumList count]) {
        NSString *identify = @"subAlbumName";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identify];
        if (cell == nil) {
            cell = [[UITableViewCell alloc] init];
        }
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(10, 0, APP_VIEW_WIDTH-20, 44)];
        label.text = [[_array_subAlbumList objectAtIndex:indexPath.row] objectForKey:@"name"];
        [cell addSubview:label];
        return cell;
        
    }
    
    return nil;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    NSDictionary *dic = [_array_subAlbumList objectAtIndex:indexPath.row];
    [self dismissViewControllerAnimated:YES completion:^{
        
        [self.delegate setAlbumPhotoDic:dic];
        
    }];

}

- (void)btnAction:(UIButton *)button {
    
    [self dismissViewControllerAnimated:YES completion:^{
        
    }];

}

- (void)listSubAlbum {
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];

    NSString* vcode = [gloabFunction getSign:@"GetShopProductAlbum" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"GetShopProductAlbum" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        if ([responseObject isKindOfClass:[NSArray class]]) {
            wself.array_subAlbumList = responseObject;
            [m_tableView reloadData];
            
        } else {
            [SVProgressHUD showSuccessWithStatus:@"数据异常"];
        }
        
        [SVProgressHUD dismiss];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
        
    }];
    
    
}



@end
