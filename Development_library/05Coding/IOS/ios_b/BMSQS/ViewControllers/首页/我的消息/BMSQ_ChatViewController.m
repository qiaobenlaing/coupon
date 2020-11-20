//
//  BMSQ_ChatViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/30.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ChatViewController.h"
#import "DXMessageToolBar.h"

#import "UIImageView+AFNetworking.h"

#import "SVProgressHUD.h"

#define IMAGEW 200


@interface BMSQ_ChatViewController ()<UITableViewDataSource,UITableViewDelegate,DXMessageToolBarDelegate>


@property (nonatomic, strong)UITableView *m_tableView;

@property (nonatomic, strong)NSMutableArray *arry;

@property (nonatomic, strong)DXMessageToolBar *chatToolBar;

@end

@implementation BMSQ_ChatViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavigationBar];
    [self setNavTitle:self.titleSr];
    [self setNavBackItem];

    [self getMsg];
    
    self.m_tableView = [[UITableView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, APP_VIEW_HEIGHT - APP_VIEW_ORIGIN_Y- [DXMessageToolBar defaultHeight])];
    self.m_tableView.dataSource = self;
    self.m_tableView.delegate = self;
    self.m_tableView.separatorStyle = UITableViewCellSelectionStyleNone;
    self.m_tableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.m_tableView];
    
    self.arry =[[NSMutableArray alloc]init];

    
    self.chatToolBar = [[DXMessageToolBar alloc] initWithFrame:CGRectMake(0, self.view.frame.size.height - [DXMessageToolBar defaultHeight], self.view.frame.size.width, [DXMessageToolBar defaultHeight])];
    self.chatToolBar.backgroundColor = [UIColor whiteColor];
    self.chatToolBar.autoresizingMask = UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleRightMargin;
    self.chatToolBar.delegate = self;
    self.chatToolBar.moreView.autoresizingMask = UIViewAutoresizingFlexibleTopMargin;
    [self.view addSubview:self.chatToolBar];

}
#pragma mark -  UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self.arry count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *dic = [self.arry objectAtIndex:indexPath.row];
    NSString *content = [dic objectForKey:@"message"];
    
    CGSize size = [content boundingRectWithSize:CGSizeMake(IMAGEW, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:15]} context:nil].size;
    return size.height <= 30 ? 75 : size.height+60;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier = @"messageIdentifier";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
        UIImageView *headImageView = [[UIImageView alloc]init];
        headImageView.tag = 200;
        [cell.contentView addSubview:headImageView];
        UIButton *headButton = [[UIButton alloc]init];
        headButton.tag = 999;
        [cell.contentView addSubview:headButton];
        
        UIImageView *backImageView = [[UIImageView alloc]init];
        backImageView.tag = 201;
        [cell.contentView addSubview:backImageView];
        
        UILabel *messageLabel = [[UILabel alloc]init];
        messageLabel.numberOfLines = 0;
        messageLabel.font = [UIFont systemFontOfSize:14];
        messageLabel.tag = 202;
        [cell.contentView addSubview:messageLabel];
        
    }
    
    UIImageView *headImageView = (UIImageView *)[cell viewWithTag:200];
    UILabel *messageLabel = (UILabel *)[cell viewWithTag:202];
    UIButton *headButton = (UIButton *)[cell viewWithTag:999];
    NSDictionary *dic = [self.arry objectAtIndex:indexPath.row];
    NSString *content = [dic objectForKey:@"message"];
    
    messageLabel.text = content;
    CGSize size = [content boundingRectWithSize:CGSizeMake(IMAGEW, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:15]} context:nil].size;
    UIImageView *backImageView = (UIImageView *)[cell viewWithTag:201];
    
    NSLog(@"---------->%@",[dic objectForKey:@"from_where"]);
    
    if (!([[dic objectForKey:@"from_where"]intValue] == 1) ) {  //1 顾客 0 店家

        [headImageView setFrame:CGRectMake(APP_VIEW_WIDTH-75, 5, 40, 40)];  //自己
        headImageView.layer.masksToBounds = YES;
        headImageView.layer.cornerRadius = 20;
        headImageView.layer.borderWidth = 1;
        headImageView.layer.borderColor = [UIColor colorWithWhite:.88 alpha:1].CGColor;
        NSString *headStr = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"logoUrl"]];
        
        [headImageView setImageWithURL:[NSURL URLWithString:headStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
        
        UIImage *backimage = [[UIImage imageNamed:@"chatto_bg_focused"] stretchableImageWithLeftCapWidth:20 topCapHeight:40];
        [backImageView setImage:backimage];
        
        CGFloat x = APP_VIEW_WIDTH-(80+size.width+30);
        backImageView.frame = CGRectMake(x, headImageView.frame.origin.y,size.width+30, size.height+20);
        messageLabel.frame = CGRectMake(backImageView.frame.origin.x + backImageView.frame.size.width+15, backImageView.frame.origin.y+10, size.width, size.height);
        messageLabel.center = CGPointMake(backImageView.center.x-2, backImageView.center.y);
        messageLabel.textColor = UICOLOR(182, 0, 12, 1.0);
        
    }else{
        [headImageView setFrame:CGRectMake(20, 10,40, 40)];
        headImageView.layer.masksToBounds = YES;
        headImageView.layer.cornerRadius = 20;
        headImageView.layer.borderWidth = 1;
        headImageView.layer.borderColor = [UIColor colorWithWhite:.88 alpha:1].CGColor;
        
        headButton.frame = headImageView.frame;
        
        
        NSString *headStr = [NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"userAvatar"]];
        
        [headImageView setImageWithURL:[NSURL URLWithString:headStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
        
//     chatfrom_bg_focused
        UIImage *backimage = [[UIImage imageNamed:@"chatfrom_bg_normal"] stretchableImageWithLeftCapWidth:20 topCapHeight:40];
        [backImageView setImage:backimage];
        
        backImageView.frame = CGRectMake(headImageView.frame.origin.x+headImageView.frame.size.width+8, headImageView.frame.origin.y, size.width+30, size.height+20);
        messageLabel.frame = CGRectMake(backImageView.frame.origin.x+backImageView.frame.size.width+15, backImageView.frame.origin.y+10, size.width, size.height);
        messageLabel.center = CGPointMake(backImageView.center.x+2, backImageView.center.y);
//
    }
    
    
    return cell;
}

-(void)sendMessage:(NSString *)messStr{
    [self didSendText:messStr];
}

-(void)getMsg{
    [self initJsonPrcClient:@"1"];
    
    [SVProgressHUD showWithStatus:@""];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:self.userCode forKey:@"userCode"];
    [params setObject:@"1" forKey:@"page"];
    [params setObject:@"" forKey:@"staffCode"];

    NSString* vcode = [gloabFunction getSign:@"getMsg" strParams:self.userCode];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"getMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        
        if([responseObject objectForKey:@"msgList"]){
            [weakSelf.arry addObjectsFromArray:[responseObject objectForKey:@"msgList"]];
            
        }
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

        NSLog(@"error");
        
    }];

    
}

-(void)didSendText:(NSString *)text{
    
    [self initJsonPrcClient:@"1"];
    [SVProgressHUD showWithStatus:@""];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:self.userCode forKey:@"userCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"staffCode"];
    [params setObject:text forKey:@"message"];
    
    
    NSString* vcode = [gloabFunction getSign:@"sendMsg" strParams:[gloabFunction getShopCode]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"sendMsg" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [SVProgressHUD dismiss];
        
        
        NSDictionary *dic = @{@"from_where":@"0",@"message":text,@"logoUrl":@"/Public/Uploads/20150929/560a3876d40dc.png"};
        [weakSelf.arry addObject:dic];
        
        weakSelf.chatToolBar.inputTextView.text = @"";
        
        
        [weakSelf.m_tableView reloadData];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"error");
        [SVProgressHUD dismiss];
        weakSelf.chatToolBar.inputTextView.text = @"";
    }];
}

@end
